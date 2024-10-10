package org.example

import java.io.File

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {

    val gestorFich = GestorFich()
    val fich = File("C:\\Users\\UsuarioT\\PROG\\ADA\\ADA_01_03\\src\\main\\resources\\empleados.csv")
    val fichAModificar = File("C:\\Users\\UsuarioT\\PROG\\ADA\\ADA_01_03\\src\\main\\resources\\empleados2.xml")

    if (!fichAModificar.exists()) {
        val listaEmpleados = gestorFich.lectorFich(fich)
        gestorFich.escritorFich(listaEmpleados, fichAModificar)
    } else {
        println("El archivo XML ya existe. Procediendo a modificarlo.")
    }

    gestorFich.modificarFich(fichAModificar)
}
