package no.uio.microobject.type

import org.antlr.v4.runtime.ParserRuleContext

//Error Messages
enum class Severity { WARNING, ERROR }
data class TypeError(val msg: String, val line: Int, val severity: Severity)

open class TypeErrorLogger {
    val stdOffset = this::class.java.classLoader.getResource("StdLib.smol").readText().lines().size+2
    //Final output: collected errors
    internal var error : List<TypeError> = listOf()

    /* interface: prints all errors and returns whether one of them is an error */
    open fun report(silent : Boolean = false) : Boolean {
        var ret = true
        for( e in error ){
            if(e.severity == Severity.ERROR) ret = false
            if(!silent) println("Line ${e.line}, ${e.severity}: ${e.msg}")
        }
        return ret
    }

    /* adds new error/warning */
    fun log(msg: String, node : ParserRuleContext?, severity: Severity = Severity.ERROR){

        error = error + TypeError(msg, (node?.getStart()?.line ?: 0 ) - stdOffset, severity)
    }

}
