#!/bin/bash

#Druid_Data
root_path=/data/docker/druid
data_folders=("metadata_data" "middle_var" "historical_var" "broker_var" "coordinator_var" "router_var" "druid_shared")

for dir in ${data_folders[@]}; do
    wd=$root_path/$dir

    if [[ -d $wd ]]; then
        echo "Removing " $wd
        sudo rm -rf $wd
    fi
    
    sudo mkdir -p $wd
    sudo chown nobody:nogroup $wd
    sudo chmod 777 $wd -R
done