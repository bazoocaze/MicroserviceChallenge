#!/bin/bash


RESPONSE_QUEUE=response.queue
INSERTION_QUEUE=insertion.queue
VALIDATION_QUEUE=validation.queue
RESPONSE_EXCHANGE=response.exchange
RESPONSE_ROUTING_KEY=response.routing.key
PUT_INSERTION_QUEUE="put.${INSERTION_QUEUE}"
PUT_VALIDATION_QUEUE="put.${VALIDATION_QUEUE}"
DBNAME="axr_challenge"


EXEC()
{
local ret
echo "EXEC: $*"
"$@" ; ret=$?
[ "$ret" != 0 ] && echo "[exit=$ret]"
return "$ret"
}


os_rabbitmqadmin()
{
local target="rabbitmqadmin"
local rep

for rep in 1 2 ; do

	if [ -f "./${target}" ] ; then
		"./${target}" "$@"
		return $?
	fi

	if type -p "${target}" ; then
		"${target}" "$@"
		return $?
	fi

	curl -f "http://localhost:15672/cli/rabbitmqadmin" >"${target}.tmp" && {
		mv -f "${target}.tmp" "${target}"
		chmod 755 "${target}"
	}

done

"${target}" "$@"
return $?
}


get_json_str()
{
local val="$1"
if [ "$val" = "null" ] ; then
	printf "null"
else
	printf "\"%s\"" "${val}"
fi
}


get_validation_request()
{
local cli=$(get_json_str "$1")
local url=$(get_json_str "$2")
local cor="$3"
echo "{ \"client\": ${cli}, \"url\": ${url}, \"correlationId\": ${cor} }"
}


get_insert_request()
{
local cli=$(get_json_str "$1")
local reg=$(get_json_str "$2")
echo "{ \"client\": ${cli}, \"regex\": ${reg} }"
}


cmd_declare()
{
EXEC os_rabbitmqadmin declare queue name="$INSERTION_QUEUE"
EXEC os_rabbitmqadmin declare queue name="$VALIDATION_QUEUE"
EXEC os_rabbitmqadmin declare queue name="$RESPONSE_QUEUE"
EXEC os_rabbitmqadmin declare exchange name="$RESPONSE_EXCHANGE" type="topic"
EXEC os_rabbitmqadmin declare binding source="$RESPONSE_EXCHANGE" destination="$RESPONSE_QUEUE" routing_key="$RESPONSE_ROUTING_KEY"

EXEC os_rabbitmqadmin declare exchange name="$PUT_INSERTION_QUEUE" type="topic"
EXEC os_rabbitmqadmin declare exchange name="$PUT_VALIDATION_QUEUE" type="topic"
EXEC os_rabbitmqadmin declare binding source="$PUT_INSERTION_QUEUE" destination="$INSERTION_QUEUE" routing_key=""
EXEC os_rabbitmqadmin declare binding source="$PUT_VALIDATION_QUEUE" destination="$VALIDATION_QUEUE" routing_key=""
}


cmd_list_response()
{
EXEC os_rabbitmqadmin get queue=response.queue  count=20 requeue=false
}


cmd_list_whitelist()
{
EXEC mysql -h 127.0.0.1 -u root -psecret -D "$DBNAME" -e "select * from url_whitelist;"
}


cmd_list()
{
local op="$1"
shift
case $op in
	resp|re)      cmd_list_response ;;
	whitelist|wh) cmd_list_whitelist ;;
	*) cmd_help ;;
esac
}


cmd_insertion()
{
local msg=$(get_insert_request "$1" "$2")
echo "MSG = $msg"
EXEC os_rabbitmqadmin publish routing_key="" exchange="$PUT_INSERTION_QUEUE" payload="$msg"
}


cmd_validation()
{
local msg=$(get_validation_request "$1" "$2" "$3")
echo "MSG = $msg"
EXEC os_rabbitmqadmin publish routing_key="" exchange="$PUT_VALIDATION_QUEUE" payload="$msg"
}


cmd_mysql()
{
EXEC mysql -h 127.0.0.1 -u root -psecret -D "$DBNAME"
}


cmd_help()
{
echo "Comandos disponíveis:"
echo " declare"
echo " insertion  cliId regex"
echo " validation cliId url corrId"
echo " list resp"
echo " list whitelist"
echo " whitelist"
echo " response"
echo " mysql"
exit 1
}


op="$1"
shift

case $op in
	declare|dec)    cmd_declare ;;
	insertion|ins)  cmd_insertion "$@" ;;
	validation|val) cmd_validation "$@" ;;
	list|li)        cmd_list "$@" ;;
	wh|whitelist)   cmd_list_whitelist ;;
	resp|response)  cmd_list_response ;;
	my|mysql)       cmd_mysql ;;
	*)              cmd_help ;;
esac
