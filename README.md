二、HBASE
#创建表
create 'relations', {NAME => 'friends', VERSIONS => '1'}
#添加数据
put 'relations', 'uid1', 'friends:uid2', 'uid2'
put 'relations', 'uid1', 'friends:uid3', 'uid3'
put 'relations', 'uid1', 'friends:uid4', 'uid4'
put 'relations', 'uid2', 'friends:uid1', 'uid1'
put 'relations', 'uid2', 'friends:uid3', 'uid3'
put 'relations', 'uid3', 'friends:uid1', 'uid1'
# 上传处理器
hadoop fs -mkdir /processor
hadoop fs -put relations_processor.jar /processor
# 挂载
alter 'relations',METHOD =>'table_att','Coprocessor'=>'hdfs://linux121:9000/processor/relations_processor.jar|com.donaldy.hbase.homework.DeleteRelationsProcessor|1001|'
# 测试删除
delete 'relations', 'uid1', 'friends:uid2'


三、Azkaban
#hive创建表
create table user_clicks(id string,click_time string,index string)
partitioned by(dt string) row format delimited fields terminated by '\t' ;
#指标表
create table user_info(active_num string,`date` string)
row format delimited fields terminated by '\t' ;
# 导入本地数据
LOAD DATA LOCAL INPATH '/root/data/click.txt' OVERWRITE INTO TABLE user_clicks PARTITION (dt='20200621');

#开发思路
1、写 job 脚本
2、写 shell 脚本
3、写 hive sql
4、上传至 azkaban
5、调度设置，调度


#1、创建 import.job
type=command
command=sh import.sh
#创建import.sh
#!/bin/sh
echo 'import data from hdfs。。。'
currDate=`date +%Y%m%d`
echo "现在时间：'$currDate'"
/opt/lagou/servers/hive-2.3.7/bin/hive -e "USE default;LOAD DATA INPATH '/user_clicks/$currDate/*' OVERWRITE INTO TABLE user_clicks PARTITION (dt='$currDate');"

#2、创建 analysis.job
type=command
dependencies=import
command=sh analysis.sh
#创建analysis.sh
#!/bin/sh
echo 'analysis user click。。。'
currDate=`date +%Y-%m-%d`
echo "现在时间：'$currDate'"
/opt/lagou/servers/hive-2.3.7/bin/hive -e "USE default;INSERT INTO TABLE user_info SELECT COUNT(DISTINCT id) active_num, TO_DATE(click_time) `date` FROM user_clicks WHERE TO_DATE(click_time) = '$currDate' GROUP BY TO_DATE(click_time);"
#sql如下
INSERT INTO TABLE user_info
SELECT COUNT(DISTINCT id), TO_DATE(click_time)
FROM user_clicks
WHERE TO_DATE(click_time) = '2021-04-15'
GROUP BY TO_DATE(click_time);










