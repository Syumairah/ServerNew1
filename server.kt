package server

import common.CustomSerializer
import common.Operator
import common.Response
import java.net.ServerSocket
import java.nio.charset.Charset
import java.util.*

fun main(args : Array<String>){

    val th = Thread({

        val server = ServerSocket(9999)
        println("Server running on port ${server.localPort}")

        while (true) {

            val client = server.accept()
            println("Client conected : ${client.inetAddress.hostAddress}")

            val serializer = CustomSerializer()

            val scanner = Scanner(client.inputStream)
            while (scanner.hasNextLine()) {
                val text = scanner.nextLine()
                val requestBytes = text.toByteArray(Charset.defaultCharset())
                val request = serializer.DeserializeRequest(requestBytes)

                println("${request.operandA} ${request.operator} ${request.operandB}")

                val response = calculate(request.operandA, request.operandB, request.operator)
                println(response)
            }

            scanner.close()
            client.close()
        }

        server.close()

    }).start()

}

fun  calculate(operandA: Int, operandB: Int, operator: operator): Response 
{
    when(operator){
        operator.Sum -> return Response(operandA + operandB, true)
        operator.Subtract -> return Response(operandA - operandB, true)
        operator.Divide -> return Response(operandA * operandB, true)
        operator.Multiply -> return Response(operandA / operandB, true)
        
        else -> return Response(0, false)
    }
}
