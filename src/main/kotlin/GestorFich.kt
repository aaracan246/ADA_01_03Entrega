package org.example
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.Text
import java.io.File
import java.nio.file.Path
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Source
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class GestorFich {

    fun lectorFich(file: File): List<Empleado>{

        val listaEmpleado = mutableListOf<Empleado>()

        file.forEachLine { linea ->
            val contenido = linea.split(",")
            if (contenido[0] != "ID"){
                val id = contenido[0].toIntOrNull() ?: 0 // <- Lo convierto a Int porque de lo contrario al ordenarlo sigue el patrón 1, 11, 12 . . . en lugar de 1, 2, 3 . . .
                val apellido = contenido[1]
                val departamento = contenido[2]
                val salario = contenido[3].toDoubleOrNull() ?: 0.0
                listaEmpleado.add(Empleado(id, apellido, departamento, salario))
            }
        }

        return listaEmpleado.sortedBy { it.id }
    }

    fun escritorFich(list: List<Empleado>, file: File){

        val db = DocumentBuilderFactory.newInstance()
        val builder = db.newDocumentBuilder()
        val imp = builder.domImplementation

        val document: Document = imp.createDocument(null, "empleados", null)

        for (i in list) {
            val empleado: Element = document.createElement("empleado")
            document.documentElement.appendChild(empleado)
            empleado.setAttribute("id", "${i.id}")


            val apellido: Element = document.createElement("apellido")
            val departamento: Element = document.createElement("departamento")
            val salario: Element = document.createElement("salario")

            empleado.appendChild(apellido)
            empleado.appendChild(departamento)
            empleado.appendChild(salario)

            val textoApellido: Text = document.createTextNode(i.apellido)
            val textoDepartamento: Text = document.createTextNode(i.departamento)
            val textoSalario: Text = document.createTextNode(i.salario.toString())

            apellido.appendChild(textoApellido)
            departamento.appendChild(textoDepartamento)
            salario.appendChild(textoSalario)

        }

        val source1: Source = DOMSource(document)

        val result: StreamResult = StreamResult(Path.of("C:\\Users\\UsuarioT\\PROG\\ADA\\ADA_01_03\\src\\main\\resources\\empleados2.xml").toFile())

        val transformer: Transformer = TransformerFactory.newInstance().newTransformer()

        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        transformer.transform(source1, result)
    }

    fun modificarFich(file: File){

        val listaEmpleados = lectorXML(file)
        val emplAModificar = encontrarEmpl(listaEmpleados)

        println("Por favor, introduzca el nuevo salario: ")
        var salarioNuevo = readln().toDoubleOrNull()
        while (salarioNuevo == null){

            println("Tipo de dato incorrecto. Por favor, introduzca el nuevo salario: ")
            salarioNuevo = readln().toDoubleOrNull()
        }

        emplAModificar?.salario = salarioNuevo

        println("La lista de empleados ha sido actualizada.")
        escritorFich(listaEmpleados, file)

    }
    private fun lectorXML(file: File): MutableList<Empleado> {
        val dbf: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()

        val db = dbf.newDocumentBuilder()

        val document = db.parse(file)

        val root: Element = document.documentElement

        root.normalize()

        val listaNodos = root.getElementsByTagName("empleado")

        val listaEmpleados = mutableListOf<Empleado>()

        for (i in 0 until listaNodos.length){

            val nodo = listaNodos.item(i)

            if (nodo.nodeType == Node.ELEMENT_NODE){
                val nodoElemento = nodo as Element

                val id = nodoElemento.getAttribute("id").toIntOrNull() ?: 0
                val elementoApellido = nodoElemento.getElementsByTagName("apellido")
                val elementoDepartamento = nodoElemento.getElementsByTagName("departamento")
                val elementoSalario = nodoElemento.getElementsByTagName("salario")

                val textContentApellido = elementoApellido.item(0).textContent
                val textContentDepartamento = elementoDepartamento.item(0).textContent
                val textContentSalario = elementoSalario.item(0).textContent.toDouble()

                val empleado = Empleado(id, textContentApellido, textContentDepartamento, textContentSalario)
                listaEmpleados.add(empleado)
            }

        }
        println(listaEmpleados)
        return listaEmpleados

    }



    private fun encontrarEmpl(list: List<Empleado>): Empleado? {

        println("Por favor, introduzca el ID a buscar: ")
        var numABuscar = readln().toIntOrNull()
        while (numABuscar == null){

            println("Tipo de dato incorrecto. Por favor, introduzca el ID a buscar: ")
            numABuscar = readln().toIntOrNull()
        }
        list.find { it.id == numABuscar }

        return list.find { it.id == numABuscar }
    }
}