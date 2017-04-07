##Ejercicio 5: Crear a partir del contenedor anterior una imagen persistente con commit.

Utilizando un contenedor creado con el Dockerfile diseñado para el hito 4, sigo el comando con la siguiente sintaxis:

```
sudo docker commit id_contenedor nuevoNombre
```

Para obtener el id del contenedor al que queremos hacer el commit utilizo el comando: "sudo docker ps", con el que se obtiene el identificador corto para cada contenedor que exista (que también es válido, a parte del identificador largo). Todo el proceso puede observarse en la siguiente captura de pantalla: [captura 1](https://github.com/manuelbr/ejercicios_CC/blob/master/tema_4/ejercicio_5/imagenes/ej5_1.png) , y aquí se puede comprobar el resultado obtenido: [captura 2](https://github.com/manuelbr/ejercicios_CC/blob/master/tema_4/ejercicio_5/imagenes/ej5_2.png). 

