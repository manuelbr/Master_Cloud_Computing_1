##Instalar una distro tal como Alpine y conectarse a ella usando el nombre de usuario y clave que indicará en su creación

Lo primero de todo es la instalación de la distro de alpine con el comando:

```
sudo lxc-create -t alpine -n alpineDock
```

En la captura siguiente podemos observar la salida que se ha de obtener: [captura 1](https://github.com/manuelbr/ejercicios_CC/blob/master/tema_4/ejercicio_2/imagenes/ej2_1.png). A continuación hacemos: "sudo lxc-start -n alpineDock" para arrancar alpine, y después "sudo lxc-attach -n alpineDock" para poder entrar a ella, tal y como puede verse [aquí](https://github.com/manuelbr/ejercicios_CC/blob/master/tema_4/ejercicio_2/imagenes/ej2_2.png).
