 docker network create \
    --driver=bridge \
    --subnet=172.18.0.0/23 \
    sr_cluster_network

docker-compose --env-file .env -f docker-compose.yml down

root_path=/data
data_folders=("fluentd" "kafka" "zookeeper" "cassandra" "prometheus" "grafana" "superset" "zeppelin" "solr")
for dir in ${data_folders[@]}; do
    wd=$root_path/$dir

    if [[ -d $wd ]]; then
        echo "Removing " $wd
        sudo rm -rf $wd
    fi
    
    sudo mkdir -p $wd
    sudo chown nobody:nogroup $wd
    sudo chmod 777 $wd -R
    echo "Created " $wd
done

docker-compose --env-file .env -f docker-compose.yml build 
docker-compose --env-file .env -f docker-compose.yml up -d

#configure for 1st time usage.
echo
echo "init superset***"
echo
bash superset/init.sh
echo "init solr****"
echo
bash solr/init.sh
echo "init cassandra*****"
echo
bash cassandra/init.sh
echo
echo "wait for solr up zzzzzzz"
echo
sleep 60
echo "create supersetDB*****"
echo
bash superset/createDB.sh