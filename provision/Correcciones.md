## Correcciones del script de Manuel Blanco Rienda

## Nombre del alumno:
José Luis Fernández Aguilera

## Errores encontrados:
No se ha encontrado ningún error referente a la ejecución del script en una máquina correactamente preparada para ser provisionada con ansible.

## Opinión:
El script realiza muchas tareas y en algunos ámbitos es bueno, pero dependiendo del servicio que vayamos a provisionar puede que nos sea más sencillo dividir el script en varios, aún así el script es muy útil y fácil de ejecutar en cualquier distribución con apt y python2.7.

## Documentación

En el siguiente enlace se puede observar la captura de pantalla de la ejecución del script en una máquina virtual Ubuntu14 en los servidores de trystack.
[Captura de pantalla](https://raw.githubusercontent.com/okynos/ProyectoCC/gh-pages/images/capturaManuel.png)

## Nombre del alumno:
José Manuel Moya Baena (jose999)

## Errores encontrados:
No se encontro ningun erro, pero si que se mostraba un warning en la ejecucion advirtiendo de que el metodo estaba "deprecated". Se ha subsanado en el codigo.

## Opinión:
Al igual que el anterior compañero, opino que estaria mejor divir los distintos servicios en distintos archivos de provisionamiento permitiendo asi
instalarlo por partes.

## Documentación

En esta imagen se puede observar el error anteriormente comentado:
![alt text](https://github.com/manuelbr/Proyecto_CC/blob/master/provision/imagenesCorrecciones/error.png "Imagen Correccion 1")

En esta otra se puede ver como el se provisiona correactamente:
![alt text](https://github.com/manuelbr/Proyecto_CC/blob/master/provision/imagenesCorrecciones/instalacion.png "Imagen Correccion 2")
