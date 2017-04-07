##Ejercicio 3: Crear un script para provisionar `nginx` o cualquier otro servidor web que pueda ser útil para alguna otra práctica

Para hacerlo crearé la misma máquina virtual con ubuntu14.04 que utilicé en el ejercicio 1 y modificaré el Vagrantfile para que tenga la siguiente información:

```
Vagrant.configure("2") do |config|
 
  config.vm.box = "ubuntu/trusty64"

  config.vm.provision "shell",
	inline: "apt-get install -y apache2"
end
```

De esta forma, se especifica que será necesario provisionar apache2 como servidor web a utilizar. Tras esto, con solo ejecutar el comando "sudo vagrant up" en el mismo directorio donde está el Vagrantfile, se montará la imagen ubuntu/trusty64 y se provisionará con apache2. [captura](https://github.com/manuelbr/ejercicios_CC/tree/master/tema_3/ejercicio3/capturas/ej3_1.png)
