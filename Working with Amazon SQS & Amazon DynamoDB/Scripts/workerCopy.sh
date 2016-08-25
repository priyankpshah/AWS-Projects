cat host_list | \
while read CMD; do
    scp -i Hadoop.pem .ssh/authorized_keys ubuntu@$CMD:/home/ubuntu/.ssh/
    cp -r Worker install.sh worker.py credential.py  ubuntu@$CMD:

done

