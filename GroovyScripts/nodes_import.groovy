import static groovy.io.FileType.FILES

//graph = ConfiguredGraphFactory.create("vblock_1")
//graph = ConfiguredGraphFactory.open("vblock_1")
//graph.tx().commit()

//system.graph('SysDef').create()
//graph = SysDef.g
:plugin use tinkerpop.gephi

graph = TinkerGraph.open()

id_num = 10
mainFile = "/home/dna/Desktop/Graph/DataDump/csv/"

dir = new File(mainFile)
files = []
dir.traverse(types: FILES, maxDepth: 0){
	files.add(it)
}
attrList = []

i = 0
while(i < files.size()){
	lineNo = 0
	nextFile = files[i]
	
	if(nextFile != null && nextFile != ""){
		nextFile.eachLine{ line ->
			if(lineNo != 0){
				split = line.split(",")
				input3 = split[0]
				idnum = split[2].toInteger()
				
				newVertex = graph.addVertex(label, input3, id, idnum)
				id_num++
				
				2.upto(attrList.size() -2, {
					nullSpace = split[it]
					timeset = attrList[it]
					if((nullSpace != "na" && nullSpace != "") && timeset != "Id" && timeset != "Label"){
						newVertex.property(timeset, nullSpace)
					}
				})
			//	graph.tx().commit()
			}
			if(lineNo == 0){
				attrList = line.split(",")
			}
			lineNo++
		}
	}
	i++
}

g = graph.traversal()

lineNo = 0
new File("/home/dna/Desktop/Graph/DataDump/relations.csv").eachLine { line ->
	if(lineNo != 0){
		(input1, input2, relation) = line.split(",")*.trim()
		id1 = input1.toInteger()
		id2 = input2.toInteger()
	
		v1 = g.V().hasId(id1).next()
		v2 = g.V().hasId(id2).next()
		
		v1.addEdge(relation, v2)
//		graph.tx().commit()
	}
	lineNo++
}

:remote connect tinkerpop.gephi
:> graph
:remote config visualTraversal graph

