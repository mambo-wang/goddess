#base image
FROM mysql:5.7

ENV WORK_PATH /usr/local/work
ENV AUTO_RUN_DIR /docker-entrypoint-initdb.d

ENV FILE_0 homework.sql
ENV FILE_1 keystone.sql

ENV INSTALL_DB_SHELL install_db.sh

RUN mkdir -p $WORK_PATH
COPY mysql/*.sql $WORK_PATH/
COPY mysql/$INSTALL_DB_SHELL $AUTO_RUN_DIR/

RUN chmod a+x $AUTO_RUN_DIR/$INSTALL_DB_SHELL

#修改时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone
