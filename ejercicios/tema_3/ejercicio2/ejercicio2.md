##Ejercicio 2: Instalar una máquina virtual ArchLinux o FreeBSD para KVM, otro hipervisor libre, usando Vagrant y conectar con ella. 

* Para descargar la box de FreeBSD he ejecutado el comando: "vagrant box add FreeBsd http://iris.hosting.lv/freebsd-9.2-i386.box": [captura](https://github.com/manuelbr/ejercicios_CC/tree/master/tema_3/ejercicio2/capturas/ej2_1.png). Con ello se ha descargado y creado la box "FreeBsd" con el contenido del enlace que le he proporcionado al comando.

* Creamos un directorio, y dentro de él ejecutamos "vagrant init FreeBsd", con lo que se creará un fichero "Vagrantfile". Tras esto ejecutamos el comando "vagrant up --provider=virtualbox" en ese directorio, con lo cual se instala y levanta la máquina especificada, usando "virtualbox" como proveedor de la máquina virtual: [captura](https://github.com/manuelbr/ejercicios_CC/tree/master/tema_3/ejercicio2/capturas/ej2_2.png). NOTA: Para instalar esta distribución de FreeBsd es necesario instalar antes el paquete de plugins extra de virtualbox, ya que la imagen necesita de la virtualización USB para funcionar.

* Cuando termine de instalarse, ejecutando "vagrant ssh" ya podemos conectarnos a ella vía ssh: [captura](https://github.com/manuelbr/ejercicios_CC/tree/master/tema_3/ejercicio2/capturas/ej2_3.png)
