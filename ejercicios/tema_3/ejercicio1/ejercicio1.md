##Ejercicio 1: Instalar una máquina virtual Debian usando Vagrant y conectar con ella.

Por problemas de almacenamiento en disco, se ha instalado una máquina ubuntu 14.04. Sin embargo se trata del mismo procedimiento que si instalaramos una debian, sólo que cambiando el enlace de descarga. A continuación se detalla el proceso:

* Utilizando el comando vagrant box add nombre_de_box enlace_de_descarga, se descarga la box correspondiente y se añade al sistema vagrant preparada para instalarse: [captura](https://github.com/manuelbr/ejercicios_CC/tree/master/tema_3/ejercicio1/capturas/ej1_1.png).

* Creamos un directorio, y dentro de él ejecutamos "vagrant init", con lo que se creará un fichero "Vagrantfile". Dentro de él modificamos la línea: config.vm.box = "base" por config.vm.box = "nombre_de_box". Tras esto ejecutamos el comando "vagrant up" en ese directorio: [captura](https://github.com/manuelbr/ejercicios_CC/tree/master/tema_3/ejercicio1/capturas/ej1_2.png).

* Cuando termine de instalarse, ejecutando "vagrant ssh" ya podemos conectarnos a ella vía ssh: [captura](https://github.com/manuelbr/ejercicios_CC/tree/master/tema_3/ejercicio1/capturas/ej1_3.png)
