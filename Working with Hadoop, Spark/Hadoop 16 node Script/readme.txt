put-
config @: ~/.ssh/ 
hosts file @: /etc/hosts; on master node.

config file consist of:
1. key file which resides in ~/.ssh, if not then copy it there.
2. Public DNS of each node.
3. Hostname

hosts file consist of
1.private_ip 
2.hostname

After putting Config and hosts file at mentioned place on master node; run copyconf.sh.
