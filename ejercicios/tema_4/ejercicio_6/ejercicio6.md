##Ejercicio 6: Reproducir los contenedores creados anteriormente usando un Dockerfile.

Lo primero es la creación de un fichero "Dockerfile" que contenga el código descrito en los apuntes, a saber (se han eliminado las tres últimas órdenes que hacen referencia  un directorio inexistente):

```
FROM alpine
MAINTAINER Manuel Blanco <manuelbr@correo.ugr.es>
WORKDIR /root
CMD ["/usr/local/bin/sbt"]

RUN apk update && apk upgrade
RUN apk add git
RUN apk add curl

RUN curl -sL "http://dl.bintray.com/sbt/native-packages/sbt/0.13.13/sbt-0.13.13.tgz" -o /usr/local/sbt.tgz
RUN cd /usr/local && tar xvfz sbt.tgz
RUN mv /usr/local/sbt-launcher-packaging-0.13.13/bin/sbt-launch.jar /usr/local/bin
```

Sigo el siguiente comando para crear una imagen a partir del Dockerfile anterior, con el siguiente resultado: [captura 1](https://github.com/manuelbr/ejercicios_CC/blob/master/tema_4/ejercicio_6/imagenes/ej6_1.png):

```
sudo docker build -t manuelbr/script .
```

En él se establece "manuelbr/script" como nombre de la imagen. Para crear el contenedor correspondiente ésta y arrancarlo, ejecuto el siguiente comando:

```
sudo docker run -it manuelbr/script
```
