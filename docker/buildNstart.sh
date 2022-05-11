 docker network create \
    --driver=bridge \
    --subnet=172.18.0.0/23 \
    sr_cluster_network

components=("fluentd" "kafka" "cassandra" "prometheus" "grafana" "jaeger-cassandra-schema" "solr" "ws")

for comp in ${components[@]}; do
    docker-compose --env-file .env -f $comp/docker-compose.yml down
done

root_path=/data
data_folders=("fluentd" "kafka" "zookeeper" "cassandra" "prometheus" "grafana" "solr")
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

#druid has many folders so manage them separately
#bash druid/vol-manager.sh

for comp in ${components[@]}; do
    docker-compose --env-file .env -f $comp/docker-compose.yml build 
    docker-compose --env-file .env -f $comp/docker-compose.yml up -d
done

echo "sleep for 120 sec : cassandra schema to be ready for Jeager"
sleep 120
docker-compose --env-file .env -f jaeger/docker-compose.yml down
docker-compose --env-file .env -f jaeger/docker-compose.yml up -d

#configure superset for 1st time usage.
#bash superset/init.sh