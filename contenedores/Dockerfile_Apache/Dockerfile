FROM debian
MAINTAINER Manuel Blanco Rienda "manuelbr@correo.ugr.es"

RUN apt-get update && apt-get install -y apache2 && apt-get install -y libapache2-mod-php5 && apt-get install -y mysql-server && apt-get install -y git

EXPOSE 80

ENTRYPOINT ["/usr/sbin/apache2ctl", "-D", "FOREGROUND"]
