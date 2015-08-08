FROM ubuntu:15.04
MAINTAINER Justin Kaufman <akaritakai@gmail.com>

ENV port 8430
EXPOSE $port

RUN echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu vivid main" >> /etc/apt/sources.list.d/java.list
RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886
RUN echo "deb http://ppa.launchpad.net/cwchien/gradle/ubuntu vivid main" >> /etc/apt/sources.list.d/gradle.list
RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 9D06AF36
RUN apt-get update
RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections
RUN apt-get install -y gradle oracle-java8-installer

RUN useradd -r -U -d /opt/jfpd -m -s /usr/sbin/nologin jfpd
RUN chown jfpd:jfpd -R /opt/jfpd
RUN cd /opt/jfpd
WORKDIR /opt/jfpd

USER jfpd
ADD src ./src
ADD policy ./policy
ADD build.gradle ./build.gradle

ENTRYPOINT ["gradle", "run"]
