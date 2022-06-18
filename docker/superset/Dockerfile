FROM amancevice/superset:1.4.1
# Switching to root to install the required packages
USER root
RUN pip install mysqlclient
# Example: installing a driver to connect to Redshift
# Find which driver you need based on the analytics database
# you want to connect to here:
# https://superset.apache.org/installation.html#database-dependencies
RUN pip install pyhive sqlalchemy-solr
COPY superset_config.py /home/superset/
# Switching back to using the `superset` user
USER superset