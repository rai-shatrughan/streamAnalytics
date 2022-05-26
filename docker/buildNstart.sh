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
bash superset/init.sh
bash solr/init.sh
bash cassandra/init.sh