#¹/bin/bash


SIZE=10000


run_insert()
{
local cli="$1"
local prefix="$2"
local sufix="$3"
local n="$SIZE"
local data

while (( n-- > 0 )) ; do
	data="${prefix}${n}${sufix}"
	echo "cli=$cli prefix=$prefix data=${data}"
	./send_producao.sh ins "${cli}" "${data}" &
done
}

run_insert "null" "teste_a-" "-data"
run_insert "123"  "teste_b-" "-data"

