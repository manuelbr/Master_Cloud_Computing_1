##Ejercicio 7: Crear con docker-machine un entorno local y ejecutar en él contenedores creados con antelación.

Lo primero de todo es crearnos la máquina virtual sobre la que ejecutaremos el contenedor con docker, utilizando el siguiente comando:

```
sudo docker-machine create --driver=virtualbox maquinilla
```

Obtengo la siguiente salida: [captura 1](https://github.com/manuelbr/ejercicios_CC/blob/master/tema_4/ejercicio_7/imagenes/ej7_1.png), y descargo el contenedor que subí para la realización del hito 4 (que contenía apache, php, mysql y git) observando lo siguiente: [captura 2](https://github.com/manuelbr/ejercicios_CC/blob/master/tema_4/ejercicio_7/imagenes/ej7_2.png).
