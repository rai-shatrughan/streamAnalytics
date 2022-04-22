components=("sr" "ws" "kafka" "superset" "ignite" "druid")

for comp in ${components[@]}; do
    docker-compose --env-file .env -f $comp/docker-compose.yml down
done

root_path=/data/docker
data_folders=("kafka" "zookeeper" "superset" "ignite" "druid")
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
bash druid/vol-manager.sh

for comp in ${components[@]}; do
    docker-compose --env-file .env -f $comp/docker-compose.yml build 
    docker-compose --env-file .env -f $comp/docker-compose.yml up -d
done

#configure superset for 1st time usage.
bash superset/init.sh