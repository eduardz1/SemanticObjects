package no.uio.microobject.runtime

import no.uio.microobject.data.LiteralExpr
import no.uio.microobject.data.Statement
import no.uio.microobject.main.Settings
import no.uio.microobject.type.*
import java.util.*

//This will be used for snapshots
class State(initStack  : Stack<StackEntry>, initHeap: GlobalMemory, simMemory: SimulationMemory, initInfo : StaticTable, private val settings: Settings) {
    private val stack: Stack<StackEntry> = initStack.clone() as Stack<StackEntry>
    private val heap: GlobalMemory = initHeap.toMutableMap()
    private val staticInfo: StaticTable = initInfo.copy()
    private val simulation : SimulationMemory = simMemory.toMap().toMutableMap()

    companion object{
        val HEADER =
        """
        @prefix owl: <http://www.w3.org/2002/07/owl#> .
        @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
        @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .
        @prefix xsd: <http://www.w3.org/2001/XMLSchema#> .
        """.trimIndent()

        val VOCAB = this::class.java.classLoader.getResource("vocab.owl").readText()

        val MINIMAL =
        """                     
        smol:null rdf:type owl:NamedIndividual , smol:Object .
        prog:_Entry_ rdf:type owl:NamedIndividual , smol:Class .
        
        """.trimIndent()
    }

}



/*
We use the term "heap" NOT in the sense of C and other low-level here.
Heap memory is barely the opposite of local memory, we have no assumptions about the memory.
 */
typealias Memory = MutableMap<String, LiteralExpr>       // Maps variable names to values
typealias GlobalMemory = MutableMap<LiteralExpr, Memory>  // Maps object name literals to local memories
typealias SimulationMemory = MutableMap<LiteralExpr, SimulatorObject>  // Maps object name literals to local memories
typealias FieldEntry = List<FieldInfo>                   //list of fields
typealias MethodEntry = Pair<Statement, List<String>> //method body and list of parameters

enum class Visibility { PUBLIC, PROTECTED, PRIVATE}

data class FieldInfo(val name: String, val type: Type, val computationVisibility : Visibility, val inferenceVisibility: Visibility, val declaredIn : Type)

data class StaticTable(
    val fieldTable: Map<String, FieldEntry>,               // This maps class names to their fields
    val methodTable: Map<String, Map<String, MethodEntry>>, // This maps class names to a map that maps method names to their definition
    val hierarchy: MutableMap<String, MutableSet<String>> = mutableMapOf()
) { // DOWNWARDS class hierarchy
    override fun toString(): String =
"""
Class Hierarchy : $hierarchy 
FieldTable      : $fieldTable 
MethodTable     : $methodTable 
""".trimIndent()

    private fun getSuper(name : String) : String?{
        for(obj in hierarchy.entries){
            for(obj2 in obj.value){
                if(obj2 == name) return obj.key
            }
        }
        return null
    }

    fun getSuperMethod(className : String, methodName : String) : MethodEntry?{
        var current = getSuper(className)
        while(current != null && current != "Object"){
            if(!methodTable.containsKey(current)) return null
            if(methodTable[current]!!.containsKey(methodName)) return methodTable[current]!![methodName]
            current = getSuper(current)
        }
        return null
    }

    // TODO: remove this completely eventually
    fun dumpClasses() : String{
        return ""
        // var res = ""
        // for(obj in fieldTable){
        //     res += "prog:${obj.key} rdf:type smol:Class.\n"
        //     res += "prog:${obj.key} rdf:type owl:Class.\n"
        //     for(obj2 in obj.value){
        //         val fieldName = obj.key+"_"+obj2.name
        //         res += "prog:${obj.key} smol:hasField prog:$fieldName.\n"
        //         res += "prog:$fieldName rdf:type smol:Field.\n"
        //         if(obj2.type == INTTYPE || obj2.type == STRINGTYPE) {
        //             res += "prog:$fieldName rdf:type owl:DatatypeProperty.\n"
        //         } else {
        //             res += "prog:$fieldName rdf:type owl:FunctionalProperty.\n"
        //             res += "prog:$fieldName rdf:type owl:ObjectProperty.\n"
        //         }
        //         res += "prog:$fieldName rdfs:domain prog:${obj.key}.\n"
        //     }
        // }

        // //records all methods
        // for(obj in methodTable){
        //     for(obj2 in obj.value){
        //         val metName = obj.key+"_"+obj2.key
        //         res += "prog:${obj.key} smol:hasMethod prog:$metName.\n"
        //         res += "prog:$metName rdf:type owl:NamedIndividual , smol:Method.\n"
        //     }
        // }


        // var all = methodTable.keys
        // //records type hierarchy
        // for(obj in hierarchy.entries){
        //     for(obj2 in obj.value){
        //         res += "prog:$obj2 rdfs:subClassOf prog:${obj.key}.\n"
        //         all -= obj2
        //     }
        // }
        // for(obj in all)
        //     res += "prog:$obj rdfs:subClassOf prog:Object.\n"

        // return res
    }
}

data class StackEntry(val active: Statement, val store: Memory, val obj: LiteralExpr, val id: Int)
