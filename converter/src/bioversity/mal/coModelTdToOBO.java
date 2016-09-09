package bioversity.mal;

import java.io.IOException;

import org.obolibrary.oboformat.model.Clause;
import org.obolibrary.oboformat.model.Frame;
import org.obolibrary.oboformat.model.FrameMergeException;
import org.obolibrary.oboformat.model.OBODoc;
import org.obolibrary.oboformat.model.Frame.FrameType;
import org.obolibrary.oboformat.parser.OBOFormatParser;
import org.obolibrary.oboformat.parser.OBOFormatParserException;
import org.obolibrary.oboformat.writer.OBOFormatWriter;


public class coModelTdToOBO {

	private OBODoc m = new OBODoc();
	
	//private static String uri = null;
	
	protected static String NL = System.getProperty("line.separator") ;
	
	
	public coModelTdToOBO(){
		Frame frameHeader = new Frame(FrameType.HEADER);
		m.setHeaderFrame(frameHeader);
		frameHeader.addClause(new Clause("default-namespace","cco"));
		frameHeader.addClause(new Clause("auto-generated-by","java"));
		
	}
	
	public coModelTdToOBO(String file){
		OBOFormatParser p = new OBOFormatParser();
		try {
			m = p.parse(file);
		} catch (OBOFormatParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println( "We have loaded a OBO model with "+m.getTypedefFrames().size()+" stranzas");
        
       
	}
	
	public void save(String file){
		
		OBOFormatWriter output = new OBOFormatWriter();
		try {
			output.write(m, file);
			System.out.println("save!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

//	public Frame setConcept(String trait, String id, String def, String crop, String lang) throws FrameMergeException{
//		lang = lang.toLowerCase();
//		id=id.trim();trait=trait.trim();crop=crop.trim();
//		id=id.replaceAll(" ", "");
//		
//		Frame frameTest = new Frame(FrameType.TERM);
//		
//		frameTest.setId(id);
//		frameTest.addClause(new Clause("id", id));
//		frameTest.addClause(new Clause("name", trait));
//		frameTest.addClause(new Clause("namespace", crop+"Trait"));
//		
//		//definition
//		if(def!=null){
//			def=def.trim();
//			frameTest.addClause(new Clause("def", def));	
//			}
//		
//		m.addTermFrame(frameTest);
//
//		return frameTest;
//	}
	// for superclasses 
	public Frame setConcept(String trait, String id, String def, String crop, String cropNum, String type, boolean b) throws FrameMergeException{
		id=id.trim().replaceAll("\"", "");
		trait=trait.trim().replaceAll("\"", "");
		crop=crop.trim().replaceAll("\"", "");
		id=id.replaceAll(" ", "");
		
		
		Frame frameTest = new Frame(FrameType.TERM);
		//System.out.println(cropNum);
		frameTest.setId(cropNum.replaceAll("\"", "")+id);
		frameTest.addClause(new Clause("id", cropNum.replaceAll("\"", "")+id));
		frameTest.addClause(new Clause("name", trait));
		frameTest.addClause(new Clause("namespace", crop+type));
		
		//definition
		if(def!=null && !def.isEmpty()){
			def=def.trim().replaceAll("\"", "");
			frameTest.addClause(new Clause("def", def));	
		}
		
		if(b){
			if(type.equalsIgnoreCase("trait")){
				frameTest.addClause(new Clause("is_a", cropNum.replaceAll("\"", "")+"1000000"));
			}else if(type.equalsIgnoreCase("method")){
				frameTest.addClause(new Clause("is_a", cropNum.replaceAll("\"", "")+"1000001"));
			}else if(type.equalsIgnoreCase("scale")){
				frameTest.addClause(new Clause("is_a", cropNum.replaceAll("\"", "")+"1000002"));
			}else if(type.equalsIgnoreCase("variable")){
				frameTest.addClause(new Clause("is_a", cropNum.replaceAll("\"", "")+"10000003"));
			}
			
			
		}
			
		
		m.addTermFrame(frameTest);

		return frameTest;
	}
	
	public Frame setConcept(String trait, String id, String def, String categoryId, String crop, String lang) throws FrameMergeException{
		lang = lang.toLowerCase().replaceAll("\"", "");
		id=id.trim().replaceAll("\"", "");;
		trait=trait.trim().replaceAll("\"", "");;
		crop=crop.trim().replaceAll("\"", "");;
		id=id.replaceAll(" ", "");

		Frame frameTest = new Frame(FrameType.TERM);

		
		frameTest.setId(id);
		frameTest.addClause(new Clause("id", id));
		if(!trait.isEmpty()){
			frameTest.addClause(new Clause("name", trait));
		}
		frameTest.addClause(new Clause("namespace", crop+"Trait"));
		
		//definition
		if(def!=null && !def.replaceAll("\"", "").isEmpty()){
			def=def.trim().replaceAll("\"", "");;
			frameTest.addClause(new Clause("def", def));	
		}
		
		if(categoryId!=null && !categoryId.replaceAll("\"", "").isEmpty()){
			if(categoryId.replaceAll("\"", "").split(":")[1].equalsIgnoreCase("Abiotic stress trait")){
				frameTest.addClause(new Clause("is_a", categoryId.replaceAll("\"", "").split(":")[0]+":"+"1000004"));
			}else if(categoryId.replaceAll("\"", "").split(":")[1].equalsIgnoreCase("Biotic stress trait")){
				frameTest.addClause(new Clause("is_a", categoryId.replaceAll("\"", "").split(":")[0]+":"+"1000005"));
			}else if(categoryId.replaceAll("\"", "").split(":")[1].equalsIgnoreCase("Phenological")){
				frameTest.addClause(new Clause("is_a", categoryId.replaceAll("\"", "").split(":")[0]+":"+"1000006"));
			}else if(categoryId.replaceAll("\"", "").split(":")[1].equalsIgnoreCase("Quality trait")){
				frameTest.addClause(new Clause("is_a", categoryId.replaceAll("\"", "").split(":")[0]+":"+"1000007"));
			}else if(categoryId.replaceAll("\"", "").split(":")[1].equalsIgnoreCase("Agronomic trait")){
				frameTest.addClause(new Clause("is_a", categoryId.replaceAll("\"", "").split(":")[0]+":"+"1000008"));
			}else if(categoryId.replaceAll("\"", "").split(":")[1].equalsIgnoreCase("Biochemical trait")){
				frameTest.addClause(new Clause("is_a", categoryId.replaceAll("\"", "").split(":")[0]+":"+"1000009"));
			}else if(categoryId.replaceAll("\"", "").split(":")[1].equalsIgnoreCase("Morphological trait")){
				frameTest.addClause(new Clause("is_a", categoryId.replaceAll("\"", "").split(":")[0]+":"+"1000010"));
			}else{
				frameTest.addClause(new Clause("is_a", categoryId.replaceAll("\"", "").split(":")[0]+":"+"1000000"));//trait
			}
		}
		
	
		//System.out.println(frameTest);
		
		m.addTermFrame(frameTest);
		//System.out.println(m);
		
		return frameTest;
	}
	
	public Clause setCreator(String name){
		Clause person = new Clause("created_by", name.trim().replaceAll("\"", ""));
		
		return person;
	}
			
	
	public void addCreatorToConcept (Clause creator, Frame concept){
		concept.addClause(creator);
	}
	
	public void addCreationDateToConcept (String date, Frame concept){
		date=date.replace("/", "T");
		concept.addClause(new Clause("creation_date", date));
	}
	
	public void addAbbrevToConcept (String abbrev, Frame concept){
		Clause syn = new Clause("synonym", abbrev.trim().replaceAll("\"", ""));
		syn.addValue("EXACT");
		concept.addClause(syn);
	}
	
	public void addAltLabel(String syn , Frame concept){
		Clause syno = new Clause("synonym", syn.trim().replaceAll("\"", ""));
		syno.addValue("EXACT");
		concept.addClause(syno);	
	}
	
	public void addDef(String def , Frame concept){
		Clause defi = new Clause("def", def.trim().replaceAll("\"", ""));
		//defi.addValue("EXACT");
		concept.addClause(defi);	
	}
	
	
//	public Resource addUsedForl(String UsedFor , Resource scheme, String lang, Resource concept){
//		Resource usedFor = m.createResource(scheme.getURI()+"/"+UsedFor.replaceAll(" ", "_"));
//		m.add(usedFor, RDF.type, SkosVoc.Concept);
//		//scheme info
//		m.add(usedFor, SkosVoc.inScheme, scheme);
//		//label info
//		m.add(usedFor, RDFS.label,UsedFor);
//		if(!concept.listProperties(SkosXLVoc.prefLabel).hasNext()){
//			Resource label = m.createResource();
//			m.add(label, RDF.type, SkosXLVoc.Label);
//			m.add(label, SkosXLVoc.literalForm, UsedFor, lang);
//			m.add(usedFor, SkosXLVoc.prefLabel, label);
//		}
//		
//		m.add(cropOntologyVoc.usedFor, RDFS.subPropertyOf, SkosVoc.related);
//		m.add(concept, cropOntologyVoc.usedFor, usedFor);
//		
//		return usedFor;
//	}
//	
	public Frame setConceptMethod(String trait, String id, String crop, String def, String lang, String source, String Traitconcept, String methodClass) throws FrameMergeException{
		lang = lang.toLowerCase().replaceAll("\"", "");
		id=id.trim().replaceAll("\"", "");
		trait=trait.trim().replaceAll("\"", "");
		crop=crop.trim().replaceAll("\"", "");
		id=id.replaceAll(" ", "");
		
		Frame concept = new Frame(FrameType.TERM);
		
		//label info
		if(trait.isEmpty()){
			trait = "Method of "+Traitconcept.replaceAll("\"", "");
		}
	
		concept.setId(id);
		concept.addClause(new Clause("id", id));
		if(!trait.isEmpty())
			concept.addClause(new Clause("name", trait));
		concept.addClause(new Clause("namespace", crop+"Method"));
		
		//definition
		if(def!=null && !def.replaceAll("\"", "").isEmpty()){
			concept.addClause(new Clause("def", def.replaceAll("\"", "")));	
		}
		
		//method class
		if(methodClass!=null && !methodClass.replaceAll("\"", "").isEmpty()){
			if(methodClass.replaceAll("\"", "").split(":")[1].equalsIgnoreCase("Measurement")){
				concept.addClause(new Clause("is_a", methodClass.replaceAll("\"", "").split(":")[0]+":"+"1000011"));	
			}else if(methodClass.replaceAll("\"", "").split(":")[1].equalsIgnoreCase("Counting")){
				concept.addClause(new Clause("is_a", methodClass.replaceAll("\"", "").split(":")[0]+":"+"1000012"));	
			}else if(methodClass.replaceAll("\"", "").split(":")[1].equalsIgnoreCase("Computation")){
				concept.addClause(new Clause("is_a", methodClass.replaceAll("\"", "").split(":")[0]+":"+"1000013"));	
			}else if(methodClass.replaceAll("\"", "").split(":")[1].equalsIgnoreCase("Estimation")){
				concept.addClause(new Clause("is_a", methodClass.replaceAll("\"", "").split(":")[0]+":"+"1000014"));	
			}else{//
				concept.addClause(new Clause("is_a", methodClass.replaceAll("\"", "").split(":")[0]+":"+"1000001"));
			}
		}
		
		//source
		if(source!=null &&(!source.isEmpty() || !source.replaceAll("\"", "").isEmpty())){
			concept.addClause(new Clause("xref", source.replaceAll("\"", "")));	
		}
		Clause rel = new Clause("relationship", "method_of");
		rel.addValue(Traitconcept.replaceAll("\"", ""));
		concept.addClause(rel);

		m.addTermFrame(concept);

		
		return concept;
	}
	
	public Frame setConceptMethod(String trait, String id, String crop, String def, String lang, String source, String Traitconcept) throws FrameMergeException{
		lang = lang.toLowerCase().replaceAll("\"", "");
		id=id.trim().replaceAll("\"", "");
		trait=trait.trim().replaceAll("\"", "");
		crop=crop.trim().replaceAll("\"", "");
		id=id.replaceAll(" ", "");
		
		Frame concept = new Frame(FrameType.TERM);
		
		//label info
		if(trait.isEmpty()){
			trait = "Method of "+Traitconcept.replaceAll("\"", "");
		}
	
		concept.setId(id);
		concept.addClause(new Clause("id", id));
		if(!trait.isEmpty())
			concept.addClause(new Clause("name", trait));
		concept.addClause(new Clause("namespace", crop+"Method"));
		
		//definition
		if(def!=null && !def.replaceAll("\"", "").isEmpty()){
			concept.addClause(new Clause("def", def.replaceAll("\"", "")));	
		}
		
		//source
		if(source!=null &&(!source.isEmpty() || !source.replaceAll("\"", "").isEmpty())){
			concept.addClause(new Clause("xref", source.replaceAll("\"", "")));	
		}
		Clause rel = new Clause("relationship", "method_of");
		rel.addValue(Traitconcept.replaceAll("\"", ""));
		concept.addClause(rel);

		m.addTermFrame(concept);

		
		return concept;
	}
	
	public Frame setExistingMethod(String id, String Traitconcept){
		Frame concept = m.getTermFrame(id.replaceAll("\"", ""));
		
		Clause rel = new Clause("relationship", "method_of");
		rel.addValue(Traitconcept.replaceAll("\"", ""));
		concept.addClause(rel);
		
		return concept;
	}
	
	public void setProperty (String prop) throws FrameMergeException{
		Frame frame = new Frame(FrameType.TYPEDEF);
		frame.setId(prop);
		frame.addClause(new Clause("id",prop));
		frame.addClause(new Clause("name",prop));
		m.addTypedefFrame(frame);
	}
	
//	public void addMethod (Resource method, Resource concept){
//		m.add(method, cropOntologyVoc.methodOf, concept);
//		m.add(cropOntologyVoc.methodOf, RDFS.subPropertyOf, SkosVoc.related);
//	}
	
	public Frame setConceptScale(String id, String scheme, String unit, String idMethod, String scaleClass) throws FrameMergeException{
		id=id.trim().replaceAll("\"", "");
		id=id.replaceAll(" ", "");
		
		Frame concept = new Frame(FrameType.TERM);
		
		concept.setId(id);
		concept.addClause(new Clause("id", id));
		
		concept.addClause(new Clause("namespace", scheme.replaceAll("\"", "")+"Scale"));
		
		//superclass
		if(scaleClass!=null && !scaleClass.replaceAll("\"", "").isEmpty()){
			if(scaleClass.replaceAll("\"", "").split(":").length>1){
				if(scaleClass.replaceAll("\"", "").split(":")[1].equalsIgnoreCase("Ordinal")){
					concept.addClause(new Clause("is_a", scaleClass.replaceAll("\"", "").split(":")[0]+":"+"1000015"));
				}else if(scaleClass.replaceAll("\"", "").split(":")[1].equalsIgnoreCase("Text")){
					concept.addClause(new Clause("is_a", scaleClass.replaceAll("\"", "").split(":")[0]+":"+"1000016"));
				}else if(scaleClass.replaceAll("\"", "").split(":")[1].equalsIgnoreCase("Nominal")){
					concept.addClause(new Clause("is_a", scaleClass.replaceAll("\"", "").split(":")[0]+":"+"1000017"));
				}else if(scaleClass.replaceAll("\"", "").split(":")[1].equalsIgnoreCase("Duration")){
					concept.addClause(new Clause("is_a", scaleClass.replaceAll("\"", "").split(":")[0]+":"+"1000018"));
				}else if(scaleClass.replaceAll("\"", "").split(":")[1].equalsIgnoreCase("Numerical")){
					concept.addClause(new Clause("is_a", scaleClass.replaceAll("\"", "").split(":")[0]+":"+"1000019"));
				}else if(scaleClass.replaceAll("\"", "").split(":")[1].equalsIgnoreCase("Time")){
					concept.addClause(new Clause("is_a", scaleClass.replaceAll("\"", "").split(":")[0]+":"+"1000020"));
				}else{//scale
					concept.addClause(new Clause("is_a", scaleClass.replaceAll("\"", "").split(":")[0]+":"+"1000002"));
				}
			}
			
		}
					
		
		//unit
		if(unit!=null){
			if(unit.isEmpty() || unit.replaceAll("\"", "").isEmpty()){
				concept.addClause(new Clause("name", "scale_of "+idMethod.replaceAll("\"", "")));
			}else{
				concept.addClause(new Clause("name", unit.replaceAll("\"", "")));
			}
		}else{
			concept.addClause(new Clause("name", id));
		}
		
		Clause rel = new Clause("relationship", "scale_of");
		rel.addValue(idMethod.replaceAll("\"", ""));
		concept.addClause(rel);
		

		m.addTermFrame(concept);
		
		return concept;
	}
	
	public Frame setConceptScale(String id, String scheme, String unit, String idMethod) throws FrameMergeException{
		id=id.trim().replaceAll("\"", "");
		id=id.replaceAll(" ", "");
		
		Frame concept = new Frame(FrameType.TERM);
		
		concept.setId(id);
		concept.addClause(new Clause("id", id));
		
		concept.addClause(new Clause("namespace", scheme.replaceAll("\"", "")+"Scale"));
		
		//unit
		if(unit!=null){
			if(unit.isEmpty() || unit.replaceAll("\"", "").isEmpty()){
				concept.addClause(new Clause("name", "scale_of "+idMethod.replaceAll("\"", "")));
			}else{
				concept.addClause(new Clause("name", unit.replaceAll("\"", "")));
			}
		}else{
			concept.addClause(new Clause("name", id));
		}
		
		Clause rel = new Clause("relationship", "scale_of");
		rel.addValue(idMethod.replaceAll("\"", ""));
		concept.addClause(rel);
		

		m.addTermFrame(concept);
		
		return concept;
	}
	
	public Frame setExistingScale(String id, String Traitconcept){
		Frame concept = m.getTermFrame(id.replaceAll("\"", ""));
		
		Clause rel = new Clause("relationship", "scale_of");
		rel.addValue(Traitconcept.replaceAll("\"", ""));
		concept.addClause(rel);
		

		return concept;
	}
	
	public Frame setConceptScale(String id, String scheme, String scaleName, String[] tab, String idMethod, String scaleClass) throws FrameMergeException{
		id=id.trim().replaceAll("\"", "");
		id=id.replaceAll(" ", "");
		Frame concept = new Frame(FrameType.TERM);
		
		concept.setId(id);
		concept.addClause(new Clause("id", id));
		
		//scale name
		if(scaleName!=null ){
			if(scaleName.isEmpty() || scaleName.replaceAll("\"", "").isEmpty()){
				concept.addClause(new Clause("name", "scale_of "+idMethod.replaceAll("\"", "")));
			}else{
				concept.addClause(new Clause("name", scaleName.replaceAll("\"", "")));
			}
			
		}else{
			concept.addClause(new Clause("name", id));
		}
		
		concept.addClause(new Clause("namespace", scheme.replaceAll("\"", "")+"Scale"));
		
		//superclass
		if(scaleClass!=null && !scaleClass.replaceAll("\"", "").isEmpty()){
			if(scaleClass.replaceAll("\"", "").split(":")[1].equalsIgnoreCase("Ordinal")){
				concept.addClause(new Clause("is_a", scaleClass.replaceAll("\"", "").split(":")[0]+":"+"1000015"));
			}else if(scaleClass.replaceAll("\"", "").split(":")[1].equalsIgnoreCase("Text")){
				concept.addClause(new Clause("is_a", scaleClass.replaceAll("\"", "").split(":")[0]+":"+"1000016"));
			}else if(scaleClass.replaceAll("\"", "").split(":")[1].equalsIgnoreCase("Nominal")){
				concept.addClause(new Clause("is_a", scaleClass.replaceAll("\"", "").split(":")[0]+":"+"1000017"));
			}else if(scaleClass.replaceAll("\"", "").split(":")[1].equalsIgnoreCase("Duration")){
				concept.addClause(new Clause("is_a", scaleClass.replaceAll("\"", "").split(":")[0]+":"+"1000018"));
			}else if(scaleClass.replaceAll("\"", "").split(":")[1].equalsIgnoreCase("Numerical")){
				concept.addClause(new Clause("is_a", scaleClass.replaceAll("\"", "").split(":")[0]+":"+"1000019"));
			}else if(scaleClass.replaceAll("\"", "").split(":")[1].equalsIgnoreCase("Time")){
				concept.addClause(new Clause("is_a", scaleClass.replaceAll("\"", "").split(":")[0]+":"+"1000020"));
			}else{//scale
				concept.addClause(new Clause("is_a", scaleClass.replaceAll("\"", "").split(":")[0]+":"+"1000002"));
			}
		}
					
		Clause rel = new Clause("relationship", "scale_of");
		rel.addValue(idMethod.replaceAll("\"", ""));
		concept.addClause(rel);

		m.addTermFrame(concept);
		
		for(int i=0;i<tab.length;i++){
			if(tab[i]!=null && !tab[i].isEmpty()){
				if(tab[i].split("=").length>=2){
					Frame cat = new Frame(FrameType.TERM);
					
					cat.setId(id+":"+tab[i].split("=")[0].replaceAll("\"", ""));
					cat.addClause(new Clause("id", id+":"+tab[i].split("=")[0].replaceAll("\"", "")));
					
					cat.addClause(new Clause("namespace", scheme.replaceAll("\"", "")+"Scale"));
					cat.addClause(new Clause("is_a", id));
					
					String[] scaleSyn = tab[i].split("=");
					if(scaleSyn.length==2){
						if(!tab[i].split("=")[1].trim().isEmpty() ||
								!tab[i].split("=")[1].trim().replaceAll("\"", "").isEmpty()){
							cat.addClause(new Clause("name", tab[i].split("=")[1].trim().replaceAll("\"", "")));
						}
						if(!tab[i].split("=")[0].trim().isEmpty() ||
								!tab[i].split("=")[0].trim().replaceAll("\"", "").isEmpty()){
							Clause syn = new Clause("synonym", tab[i].split("=")[0].trim().replaceAll("\"", ""));
							syn.addValue("EXACT");
							cat.addClause(syn);
						}
						
					}else if(scaleSyn.length>=3){
						if(!tab[i].split("=")[2].trim().isEmpty() ||
								!tab[i].split("=")[2].trim().replaceAll("\"", "").isEmpty()){
							cat.addClause(new Clause("name", tab[i].split("=")[2].trim().replaceAll("\"", "")));
						}
						if(!tab[i].split("=")[0].trim().isEmpty() ||
								tab[i].split("=")[0].trim().replaceAll("\"", "").isEmpty()){
							Clause syn = new Clause("synonym", tab[i].split("=")[0].trim().replaceAll("\"", ""));
							syn.addValue("EXACT");
							cat.addClause(syn);
						}
						if(!tab[i].split("=")[1].trim().isEmpty() || 
								!tab[i].split("=")[1].trim().replaceAll("\"", "").isEmpty()){
							Clause syn2 = new Clause("synonym", tab[i].split("=")[1].trim().replaceAll("\"", ""));
							syn2.addValue("EXACT");
							cat.addClause(syn2);
						}
						
					}else{
						if(!tab[i].split("=")[0].trim().isEmpty() ||
								!tab[i].split("=")[0].trim().replaceAll("\"", "").isEmpty())
							cat.addClause(new Clause("name", tab[i].split("=")[0].trim().replaceAll("\"", "")));
					}
					
					
					m.addTermFrame(cat);			
					}
			}
		}
		
		

		return concept;
	}
	
	public Frame setConceptScale(String id, String scheme, String scaleName, String[] tab, String idMethod) throws FrameMergeException{
		id=id.trim().replaceAll("\"", "");
		id=id.replaceAll(" ", "");
		Frame concept = new Frame(FrameType.TERM);
		
		concept.setId(id);
		concept.addClause(new Clause("id", id));
		
		//scale name
		if(scaleName!=null ){
			if(scaleName.isEmpty() || scaleName.replaceAll("\"", "").isEmpty()){
				concept.addClause(new Clause("name", "scale_of "+idMethod.replaceAll("\"", "")));
			}else{
				concept.addClause(new Clause("name", scaleName.replaceAll("\"", "")));
			}
			
		}else{
			concept.addClause(new Clause("name", id));
		}
		
		concept.addClause(new Clause("namespace", scheme.replaceAll("\"", "")+"Scale"));
					
		Clause rel = new Clause("relationship", "scale_of");
		rel.addValue(idMethod.replaceAll("\"", ""));
		concept.addClause(rel);

		m.addTermFrame(concept);
		
		for(int i=0;i<tab.length;i++){
			if(tab[i]!=null && !tab[i].isEmpty()){
				if(tab[i].split("=").length>=2){
					Frame cat = new Frame(FrameType.TERM);
					
					cat.setId(id+":"+tab[i].split("=")[0].replaceAll("\"", ""));
					cat.addClause(new Clause("id", id+"/"+tab[i].split("=")[0].replaceAll("\"", "")));
					
					cat.addClause(new Clause("namespace", scheme.replaceAll("\"", "")+"Scale"));
					cat.addClause(new Clause("is_a", id));
					
					String[] scaleSyn = tab[i].split("=");
					if(scaleSyn.length==2){
						if(!tab[i].split("=")[1].trim().isEmpty() ||
								!tab[i].split("=")[1].trim().replaceAll("\"", "").isEmpty()){
							cat.addClause(new Clause("name", tab[i].split("=")[1].trim().replaceAll("\"", "")));
						}
						if(!tab[i].split("=")[0].trim().isEmpty() ||
								!tab[i].split("=")[0].trim().replaceAll("\"", "").isEmpty()){
							Clause syn = new Clause("synonym", tab[i].split("=")[0].trim().replaceAll("\"", ""));
							syn.addValue("EXACT");
							cat.addClause(syn);
						}
						
					}else if(scaleSyn.length>=3){
						if(!tab[i].split("=")[2].trim().isEmpty() ||
								!tab[i].split("=")[2].trim().replaceAll("\"", "").isEmpty()){
							cat.addClause(new Clause("name", tab[i].split("=")[2].trim().replaceAll("\"", "")));
						}
						if(!tab[i].split("=")[0].trim().isEmpty() ||
								tab[i].split("=")[0].trim().replaceAll("\"", "").isEmpty()){
							Clause syn = new Clause("synonym", tab[i].split("=")[0].trim().replaceAll("\"", ""));
							syn.addValue("EXACT");
							cat.addClause(syn);
						}
						if(!tab[i].split("=")[1].trim().isEmpty() || 
								!tab[i].split("=")[1].trim().replaceAll("\"", "").isEmpty()){
							Clause syn2 = new Clause("synonym", tab[i].split("=")[1].trim().replaceAll("\"", ""));
							syn2.addValue("EXACT");
							cat.addClause(syn2);
						}
						
					}else{
						if(!tab[i].split("=")[0].trim().isEmpty() ||
								!tab[i].split("=")[0].trim().replaceAll("\"", "").isEmpty())
							cat.addClause(new Clause("name", tab[i].split("=")[0].trim().replaceAll("\"", "")));
					}
					
					
					m.addTermFrame(cat);			
					}
			}
		}
		
		

		return concept;
	}
	
//	public void addScale (Resource scale, Resource method){
//		m.add(scale, cropOntologyVoc.scaleOf, method);
//		m.add(cropOntologyVoc.scaleOf, RDFS.subPropertyOf, SkosVoc.related);
//	}
	
	public Frame setVariable(String var, String id, String trait, String method, String scale, String crop, String lang) throws FrameMergeException{
		lang = lang.toLowerCase().replaceAll("\"", "");
		id=id.trim().replaceAll("\"", "");
		var=var.trim().replaceAll("\"", "");
		crop=crop.trim().replaceAll("\"", "");
		id=id.replaceAll(" ", "");
		
		Frame frameTest = new Frame(FrameType.TERM);
		
		frameTest.setId(id);
		frameTest.addClause(new Clause("id", id));
		if(!var.isEmpty() || !var.replaceAll("\"", "").isEmpty()){
			frameTest.addClause(new Clause("name", var));
		}
		
		frameTest.addClause(new Clause("is_a", id.split(":")[0]+":Variable"));
		frameTest.addClause(new Clause("namespace", crop+"Variable"));
		
//		Clause rel = new Clause("intersection_of", "composed_of");
//		rel.addValue(trait.replaceAll("\"", ""));
//		frameTest.addClause(rel);
//		rel = new Clause("intersection_of", "composed_of");
//		rel.addValue(method.replaceAll("\"", ""));
//		frameTest.addClause(rel);
//		rel = new Clause("intersection_of", "composed_of");
//		rel.addValue(scale.replaceAll("\"", ""));
//		frameTest.addClause(rel);
		
		Clause rel = new Clause("relationship", "variable_of");
		rel.addValue(trait.replaceAll("\"", ""));		frameTest.addClause(rel);
		rel = new Clause("relationship", "variable_of");
		rel.addValue(method.replaceAll("\"", ""));
		frameTest.addClause(rel);
		rel = new Clause("relationship", "variable_of");
		rel.addValue(scale.replaceAll("\"", ""));
		frameTest.addClause(rel);
		
		m.addTermFrame(frameTest);

		return frameTest;
	}
	
	public Frame setVariable(String var, String id, String cat, String trait, String method, String scale, String crop, String lang) throws FrameMergeException{
		lang = lang.toLowerCase().replaceAll("\"", "");
		id=id.trim().replaceAll("\"", "");
		var=var.trim().replaceAll("\"", "");
		crop=crop.trim().replaceAll("\"", "");
		id=id.replaceAll(" ", "");
		
		Frame frameTest = new Frame(FrameType.TERM);
		
		frameTest.setId(id);
		frameTest.addClause(new Clause("id", id));
		if(!var.isEmpty() || !var.replaceAll("\"", "").isEmpty()){
			frameTest.addClause(new Clause("name", var));
		}

		//superclass
		if(cat!=null && !cat.replaceAll("\"", "").isEmpty()){
			if(cat.replaceAll("\"", "").split(":")[1].equalsIgnoreCase("supplementary variable")){
				frameTest.addClause(new Clause("is_a", cat.replaceAll("\"", "").split(":")[0]+":"+"1000021"));
			}else if(cat.replaceAll("\"", "").split(":")[1].equalsIgnoreCase("breeding variable")){
				frameTest.addClause(new Clause("is_a", cat.replaceAll("\"", "").split(":")[0]+":"+"1000022"));
			}else{//variable
				frameTest.addClause(new Clause("is_a", cat.replaceAll("\"", "").split(":")[0]+":"+"1000003"));
			}
		}
		
		frameTest.addClause(new Clause("namespace", crop+"Variable"));
		
//		Clause rel = new Clause("intersection_of", "composed_of");
//		rel.addValue(trait.replaceAll("\"", ""));
//		frameTest.addClause(rel);
//		rel = new Clause("intersection_of", "composed_of");
//		rel.addValue(method.replaceAll("\"", ""));
//		frameTest.addClause(rel);
//		rel = new Clause("intersection_of", "composed_of");
//		rel.addValue(scale.replaceAll("\"", ""));
//		frameTest.addClause(rel);
		
		Clause rel = new Clause("relationship", "variable_of");
		rel.addValue(trait.replaceAll("\"", ""));		frameTest.addClause(rel);
		rel = new Clause("relationship", "variable_of");
		rel.addValue(method.replaceAll("\"", ""));
		frameTest.addClause(rel);
		rel = new Clause("relationship", "variable_of");
		rel.addValue(scale.replaceAll("\"", ""));
		frameTest.addClause(rel);
		
		m.addTermFrame(frameTest);

		return frameTest;
	}
	
}
