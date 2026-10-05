#!/bin/sh
# Create the redis configuration directory if it doesn't exist
mkdir -p /usr/local/etc/redis

# Generate the ACL file using environment variables
echo "user default on >${CACHE_PASSWORD} +@all +HELLO ~* &*" > /usr/local/etc/redis/users.acl

# Point redis to the generated acl file via a minimal redis.conf
echo "aclfile /usr/local/etc/redis/users.acl" > /usr/local/etc/redis/redis.conf

# Start the Redis server using the generated config
exec docker-entrypoint.sh redis-server /usr/local/etc/redis/redis.conf --appendonly yes
