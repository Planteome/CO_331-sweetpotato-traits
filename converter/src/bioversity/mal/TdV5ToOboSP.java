package bioversity.mal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.obolibrary.oboformat.model.Frame;

public class TdV5ToOboSP {

	private static List<ArrayList<String> > read(String dataFileName, ArrayList<String> oRowStruct) throws Exception{
		ArrayList dataset = new ArrayList();
		try{
			BufferedReader reader = new BufferedReader( new FileReader( dataFileName ));

			int lineNo = 0;
			String line;


			while( (line = reader.readLine()) != null ) {

				//System.out.println("lineno="+lineNo);

					if(lineNo==0){
						//the first row is the data structure
						//String[] attNames = line.split(";");
						String[] attNames = line.split("\\$");

						for(int i=0;i<attNames.length;i++){
							oRowStruct.add(attNames[i].trim().replaceAll("\"", ""));
						}
					}else{
						//the late rows are data content
						ArrayList<String> row = new ArrayList<String>();

							//String[] rowValues =  line.split(";");
							String[] rowValues =  line.split("\\$");
							for(int i=0;i<rowValues.length;i++){
								row.add(rowValues[i]);
							}
							while(row.size()<oRowStruct.size()){
								row.add(null);
							}

					if(row.size()!=oRowStruct.size()){
						System.out.println("line no="+lineNo);
                        System.out.println("oRowStruct size="+oRowStruct.size()+":"+oRowStruct);
                        System.out.println("rowValues size="+row.size()+":"+row);
						throw new Exception("Data structure attribute number is different row value number.");
					}

					dataset.add(row);

					}
				//}
				lineNo++;
			}
			reader.close();
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return dataset;
	}

	public static void main(String[] args){

		/// args[0] = excel file
		if(args.length != 2){

				System.out.println("invalid number of arguments. Fist argument must be the excel TD version 5 and the second argument should be the obo file");
		}else{

			//creation of the temp file: csv file
			File temp= null;
			try {
				temp = File.createTempFile("tempfiletest", ".tmp");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        String path = temp.getAbsolutePath();
	        xlsx(new File(args[0]), temp);

			//String datafile = args[0]; //"/Users/marie-angeliquelaporte/Dropbox/toto.csv";

			//read the data from data file to data structure
			ArrayList<String> rowStruct = new ArrayList<String>();
			List dataset = null;
			try {
				dataset = TdV5ToOboSP.read(path, rowStruct);
				//System.out.println(rowStruct);

				////build empty obo model
				coModelTdToOBO m = new coModelTdToOBO();

				///creation of specific properties
				m.setProperty("method_of");
				m.setProperty("scale_of");
				m.setProperty("variable_of");

				//Crop
				String crop= "";

				///for checking if id already exists
				ArrayList<String> ids = new ArrayList<String>();
				ArrayList<String> duplicate = new ArrayList<String>();

				////creation categories
				Iterator it = dataset.iterator();
				Set cat=new HashSet();
				Set catM=new HashSet();
				Set catS=new HashSet();
				Set catV=new HashSet();

				/// get the crop id
				String cropID =  "CO_325";

				//foreach line of the file
				while(it.hasNext()){
					ArrayList<String> rowInfo = (ArrayList<String>) it.next();
					//System.out.println(rowInfo);

					//get the superclasses
					String category = rowInfo.get(rowStruct.indexOf("Trait class"));
					String categoryM = rowInfo.get(rowStruct.indexOf("Method class"));
					String categoryS = rowInfo.get(rowStruct.indexOf("Scale class"));

					//NO VAR CLASS FOR NOW
					//String categoryV = rowInfo.get(rowStruct.indexOf("Variable class"));

					//String lang = rowInfo.get(rowStruct.indexOf("Language of submission"));
					String lang = rowInfo.get(rowStruct.indexOf("Language"));

					if(rowInfo.get(rowStruct.indexOf("Trait ID"))==null){
						continue;
					}

					if (cropID.isEmpty())
						cropID = rowInfo.get(rowStruct.indexOf("Trait ID")).split(":")[0];

					try{
						if(crop.isEmpty()){
							///A remplacer avec le vrai TD
							crop = rowInfo.get(rowStruct.indexOf("Crop"));
							//crop = "Cassava";
						}
					}catch(Exception e){
						System.out.println(e + ": Crop is empty. You must complete the column Crop");
						return;
					}


					//let's say that language will be english if empty
					if(lang==null){
						lang="en";
					}


					//add category if english
					if(lang!=null && (lang.equalsIgnoreCase("\"en\"")||lang.equalsIgnoreCase("en"))
							&& category!=null && !category.isEmpty() && !category.replaceAll("\"", "").isEmpty()){
							cat.add(category.replaceAll(" ", "_"));
					}
					//MEhthos and scale classes
					if(lang!=null && (lang.equalsIgnoreCase("\"en\"")||lang.equalsIgnoreCase("en"))
							&& categoryM!=null && !categoryM.isEmpty() && !categoryM.replaceAll("\"", "").isEmpty()){
						catM.add(categoryM.replace(" ", "_"));
					}
					if(lang!=null && (lang.equalsIgnoreCase("\"en\"")||lang.equalsIgnoreCase("en"))
							&& categoryS!=null && !categoryS.isEmpty() && !categoryS.replaceAll("\"", "").isEmpty()){
						catS.add(categoryS.replace(" ", "_"));
					}
					//NO VAR CLASS FOR NOW
//					if(lang!=null && (lang.equalsIgnoreCase("\"en\"")||lang.equalsIgnoreCase("en"))
//							&& categoryV!=null && !categoryV.isEmpty() && !categoryV.replaceAll("\"", "").isEmpty()){
//						catV.add(categoryV.replace(" ", "_"));
//					}
				}

				 ///to print line number when pb
				 int rowNumber=1;

				 //create IDs when empty
				 int cpt = 812; //last id in SP is 811, for method and scale
				 //int cptmethod = 10000;
				 //int cptscale = 100000;

				 String zero = "0000"; //id has 3 digits
//				 String zeroM = "00"; //id has 3 digits
//				 String zeroS = "0"; //id has 3 digits
//				 String zeroC = ""; //id has 3 digits
				 Map<String, String> traitMap = new HashMap<String, String>();
				 Map<String, String> methodIDMap = new HashMap<String, String>();
				 Map<String, String> scaleIDMap = new HashMap<String, String>();
				 //Map<String, String> varIDMap = new HashMap<String, String>();

				//creation of the "roots" terms for trait, method and scale
				Frame traitRootClass = m.setConcept("Trait", "1000000", "", crop, cropID+":", "Trait", false);
				Frame methodRootClass = m.setConcept("Method", "1000001", "", crop, cropID+":", "Method", false);
				Frame scaleRootClass = m.setConcept("Scale", "1000002", "", crop, cropID+":", "Scale", false);
				Frame variableRootClass = m.setConcept("Variable", "1000003", "", crop, cropID+":", "Variable", false);

				Iterator itCat = cat.iterator();
				while(itCat.hasNext()){
					String category = (String) itCat.next();
					///Concept creation
					category = category.replaceAll("\"", "");
					if(category.equalsIgnoreCase("Abiotic_stress_trait")){
						Frame concept = m.setConcept(category, "1000004", "", crop, cropID+":", "Trait", true);
					}else if(category.equalsIgnoreCase("Biotic_stress_trait")){
						Frame concept = m.setConcept(category, "1000005", "", crop, cropID+":", "Trait", true);
					}else if(category.equalsIgnoreCase("Phenological")){
						Frame concept = m.setConcept(category, "1000006", "", crop, cropID+":", "Trait", true);
					}else if(category.equalsIgnoreCase("Quality_trait")){
						Frame concept = m.setConcept(category, "1000007", "", crop, cropID+":", "Trait", true);
					}else if(category.equalsIgnoreCase("Agronomic_trait")){
						Frame concept = m.setConcept(category, "1000008", "", crop, cropID+":", "Trait", true);
					}else if(category.equalsIgnoreCase("Biochemical_trait")){
						Frame concept = m.setConcept(category, "1000009", "", crop, cropID+":", "Trait", true);
					}else if(category.equalsIgnoreCase("Morphological_trait")){
						Frame concept = m.setConcept(category, "1000010", "", crop, cropID+":", "Trait", true);
					}
				}
				itCat = catM.iterator();
				while(itCat.hasNext()){
					String category = (String) itCat.next();
					category = category.replaceAll("\"", "");

					if(category.equalsIgnoreCase("Measurement")){
						Frame concept = m.setConcept(category, "1000011", "", crop, cropID+":", "Method", true);
					}else if(category.equalsIgnoreCase("Counting")){
						Frame concept = m.setConcept(category, "1000012", "", crop, cropID+":", "Method", true);
					}else if(category.equalsIgnoreCase("Computation")){
						Frame concept = m.setConcept(category, "1000013", "", crop, cropID+":", "Method", true);
					}else if(category.equalsIgnoreCase("Estimation")){
						Frame concept = m.setConcept(category, "1000014", "", crop, cropID+":", "Method", true);
					}
				}
				itCat = catS.iterator();
				while(itCat.hasNext()){
					String category = (String) itCat.next();
					category = category.replaceAll("\"", "");

					if(category.equalsIgnoreCase("Ordinal")){
						Frame concept = m.setConcept(category, "1000015", "", crop, cropID+":", "Scale", true);
					}else if(category.equalsIgnoreCase("Text")){
						Frame concept = m.setConcept(category, "1000016", "", crop, cropID+":", "Scale", true);
					}else if(category.equalsIgnoreCase("Nominal")){
						Frame concept = m.setConcept(category, "1000017", "", crop, cropID+":", "Scale", true);
					}else if(category.equalsIgnoreCase("Duration")){
						Frame concept = m.setConcept(category, "1000018", "", crop, cropID+":", "Scale", true);
					}else if(category.equals("Numerical")){
						Frame concept = m.setConcept(category, "1000019", "", crop, cropID+":", "Scale", true);
					}else if(category.equalsIgnoreCase("Time")){
						Frame concept = m.setConcept(category, "1000020", "", crop, cropID+":", "Scale", true);
					}

				}
				//specific to banana
				itCat = catV.iterator();
				while(itCat.hasNext()){
					String category = (String) itCat.next();
					category = category.replaceAll("\"", "");

					if(category.equalsIgnoreCase("supplementary_variable")){
						Frame concept = m.setConcept(category, "1000021", "", crop, cropID+":", "Variable", true);
					}else if(category.equalsIgnoreCase("breeding_variable")){
						Frame concept = m.setConcept(category, "1000022", "", crop, cropID+":", "Variable", true);
					}
				}

				ids.addAll(cat);
				ids.addAll(catM);
				ids.addAll(catS);
				ids.addAll(catV);

				///This time add the concepts
				it = dataset.iterator();

				////to remove duplicated method and scale
				 Map<String, String> methodMap = new HashMap<String, String>();
				 Map<String, String> scaleMap = new HashMap<String, String>();


				////creation traits, methods, scales, variables
				while(it.hasNext()){
					rowNumber++;
					ArrayList<String> rowInfo = (ArrayList<String>) it.next();
					//System.out.println(rowInfo);

					//some variables can be obsolete
					String keep = rowInfo.get(rowStruct.indexOf("Trait status"));

					///CHANGE THAT LATER
					if (keep!=null && keep.equalsIgnoreCase("toto")){
						continue;
					}else{
						crop = rowInfo.get(rowStruct.indexOf("Crop"));
						String traitName = rowInfo.get(rowStruct.indexOf("Trait name"));
						String coId = rowInfo.get(rowStruct.indexOf("Trait ID"));
						String def = rowInfo.get(rowStruct.indexOf("Trait description"));
						String category = rowInfo.get(rowStruct.indexOf("Trait class"));
						String lang = rowInfo.get(rowStruct.indexOf("Language"));

						//String organization = rowInfo.get(rowStruct.indexOf("Institution"));
						String date = rowInfo.get(rowStruct.indexOf("Date"));
						String abbrev = rowInfo.get(rowStruct.indexOf("Main trait abbreviation"));
						String abbrevs = rowInfo.get(rowStruct.indexOf("Alternative trait abbreviations"));
						String synonym = rowInfo.get(rowStruct.indexOf("Trait synonyms"));
						//String usedFor = rowInfo.get(rowStruct.indexOf("How is this trait routinely used?"));
						String creator = rowInfo.get(rowStruct.indexOf("Scientist"));
						//System.out.println(lang);

						if(lang!=null && (lang.equalsIgnoreCase("\"en\"")||lang.equalsIgnoreCase("en"))){
							if(coId.isEmpty() || coId.replace("\"", "").isEmpty()){
								if(traitMap.containsKey(traitName.trim().toLowerCase())){
									coId = traitMap.get(traitName.trim().toLowerCase());
								}else{
									coId = cropID+":"+zero+cpt;//concatenate to create new ID
									cpt++;
									//add the id to the map
									traitMap.put(traitName.trim().toLowerCase(), coId);
								}
							}
							if( coId!=null){
								//look for duplicate ids
								if(ids.contains(coId)){
									duplicate.add(coId);
								}
								if (coId.isEmpty() || coId.replace("\"", "").isEmpty()){
									System.out.println("Line "+ rowNumber + " has been ignored because Trait ID was empty. Fill in this column ans re-run the script to have this line info in the output obo file!");
									continue;
								}

								if(!ids.contains(coId)){
								ids.add(coId);

									 ///Concept creation
									Frame concept = m.setConcept(traitName.toLowerCase(), coId, def, cropID+":"+category, crop, lang);

									///Add creator to concept
								   if(!creator.isEmpty()){
								    	m.addCreatorToConcept(m.setCreator(creator), concept);
								    }


				//				    ///Add creation date to concept
				//				    if(!date.isEmpty()){
				//
				//				    	m.addCreationDateToConcept(date, concept);
				//				    }
				//
								    //add abbreviation and synonyms
								    if(!abbrev.isEmpty() && !abbrev.replaceAll("\"", "").isEmpty()){
								    	abbrev = abbrev.replaceAll("\"", "");
								    	String[] abbrevTab = abbrev.split(",");
								    	for(int k=0; k<abbrevTab.length; k++){
								    		m.addAbbrevToConcept(abbrevTab[k], concept);
								    	}
								    }
								    if(!abbrevs.isEmpty() && !abbrevs.replaceAll("\"", "").isEmpty()){
								    	abbrevs = abbrevs.replaceAll("\"", "");
								    	String[] abbrevTab = abbrevs.split(",");
								    	for(int k=0; k<abbrevTab.length; k++){
								    		m.addAbbrevToConcept(abbrevTab[k], concept);
								    	}								    }

								    if(!synonym.isEmpty() && !synonym.replaceAll("\"", "").isEmpty()){
								    	synonym = synonym.replaceAll("\"", "");
								    	String[] synTab = synonym.split(",");
								    	for(int k=0; k<synTab.length; k++){
								    		m.addAltLabel(synTab[k], concept);
								    	}
								    }
								}


							    ///////////////////////////////////////
							    //addMethod
							    ///////////////////////////////////////

							    String method = rowInfo.get(rowStruct.indexOf("Method name"));
							    String idMethod = rowInfo.get(rowStruct.indexOf("Method ID"));
							    String defMethod = rowInfo.get(rowStruct.indexOf("Method description"));
							    String sourceMethod = rowInfo.get(rowStruct.indexOf("Method reference"));
							    String classeMethod = rowInfo.get(rowStruct.indexOf("Method class"));
							    Frame Method = null;

							    //if(idMethod.isEmpty() || idMethod.replaceAll("\"", "").isEmpty()){
									if(methodIDMap.containsKey(method.trim().toLowerCase())){
										idMethod = methodIDMap.get(method.trim().toLowerCase());

									}else{
										idMethod = cropID+":"+zero+cpt;//concatenate to create new ID
										cpt++;
										//idMethod = cropID+":"+zeroM+cptmethod;//concatenate to create new ID
										//cptmethod++;
										methodIDMap.put(method.trim().toLowerCase(), idMethod);
									}
								//}
							    if(idMethod!=null){
								    //add method

							    	if(idMethod.isEmpty() || idMethod.replaceAll("\"", "").isEmpty()){
								    	System.out.println("Method hasn't been created because empty, line: "+rowNumber);
							    		continue;
							    	}

							    	if(methodMap.containsKey(idMethod)){
							    		duplicate.add(idMethod);
							    		m.setExistingMethod(idMethod, coId);
							    	}
							    	else{
							    		Method = m.setConceptMethod(method.toLowerCase(), idMethod, crop, defMethod, lang, sourceMethod, coId, cropID+":"+classeMethod);
							    	}
							    	methodMap.put(idMethod, "");
							    }else{
							    	System.out.println("Method hasn't been created because empty, line: "+rowNumber);
							    	continue;
							    }


								///////////////////////////////////////
								//addMeasure
								///////////////////////////////////////

							    String idMeasure = rowInfo.get(rowStruct.indexOf("Scale ID"));
								//String typeMeasure = rowInfo.get(rowStruct.indexOf("Type of Measure (Continuous, Discrete or Categorical)"));
								//String unitContinuous = rowInfo.get(rowStruct.indexOf(""));
								String scaleName = rowInfo.get(rowStruct.indexOf("Scale name"));
								String scaleClass = rowInfo.get(rowStruct.indexOf("Scale class"));
								//String categorical = rowInfo.get(rowStruct.indexOf("For Categorical: Name of rating scale"));

								//if(idMeasure.isEmpty() || idMeasure.replaceAll("\"", "").isEmpty()){
									if(scaleIDMap.containsKey(scaleName.trim().toLowerCase())){
										idMeasure = scaleIDMap.get(scaleName.trim().toLowerCase());
										//System.out.println(idMeasure);
									}else{
										idMeasure = cropID+":"+zero+cpt;//concatenate to create new ID
										cpt++;
										//idMeasure = cropID+":"+zeroS+cptscale;//concatenate to create new ID
										//cptscale++;
										scaleIDMap.put(scaleName.trim().toLowerCase(), idMeasure);
									}
								//}

								if(idMeasure!=null ){
									if(idMeasure.isEmpty()){
								    	System.out.println("Scale hasn't been created because empty, line: "+rowNumber);
										continue;
									}

									if(scaleMap.containsKey(idMeasure)){
										duplicate.add(idMeasure);
										m.setExistingScale(idMeasure, idMethod);
									}else{
										if( scaleName!=null && (scaleClass.equalsIgnoreCase("\"Nominal\"")|| scaleClass.equalsIgnoreCase("Nominal")
												|| scaleClass.equalsIgnoreCase("\"Ordinal\"") || scaleClass.equalsIgnoreCase("Ordinal"))){

											String [] scales = new String [100];
											///search how many scales exist
											int index = rowStruct.indexOf("Scale Xref")+1;
											//int fin = rowStruct.indexOf("Variable ID")-1;
											int fin = rowStruct.size()-1;
											int nbScale = 0;

											while(index<fin){
												if(rowInfo.get(index)!=null && !rowInfo.get(index).isEmpty()){
													scales[nbScale]=rowInfo.get(index);
												}

												nbScale++;
												index++;
											}
		 									Frame Scale = m.setConceptScale(idMeasure, crop, scaleName.toLowerCase(),scales,idMethod,cropID+":"+scaleClass );
											//m.addScale(Scale, Method);
										}else if (scaleName!=null && !(scaleClass.equalsIgnoreCase("\"Nominal\"")|| scaleClass.equalsIgnoreCase("Nominal")
												|| scaleClass.equalsIgnoreCase("\"Ordinal\"") || scaleClass.equalsIgnoreCase("Ordinal"))){
											//add scale
											String unit = "";
											if (scaleName==null  || scaleName.equalsIgnoreCase("")){
												//unit = scaleName;
											}else{
												unit=scaleName;
											}
											if(scaleMap.containsKey(unit)){
									    		Frame Scale = m.setExistingScale(scaleMap.get(unit), idMethod);
									    		idMeasure=scaleMap.get(unit);


									    	}else{
									    		//Frame Scale = m.setConceptScale(idMeasure, crop, unit, idMethod, scaleClass);
									    		Frame Scale = m.setConceptScale(idMeasure, crop, unit.toLowerCase(), idMethod, cropID+":"+scaleClass);
									    		scaleMap.put(unit, idMeasure);
									    	}
										}
										scaleMap.put(idMeasure, "");
										//m.addScale(Scale, Method);
									}
								}else{
							    	System.out.println("Scale hasn't been created because empty, line: "+rowNumber);
							    	continue;
								}

								///////////////////////////////////////
								//addVariable
								///////////////////////////////////////


								String idVariable;
								String varSyn = rowInfo.get(rowStruct.indexOf("Variable name"));
								String varName = rowInfo.get(rowStruct.indexOf("Variable label"));
								//String varDef = rowInfo.get(rowStruct.indexOf("Variable definition"));
							//	String categoryVar = rowInfo.get(rowStruct.indexOf("Variable class"));
				
								//String categorical = rowInfo.get(rowStruct.indexOf("For Categorical: Name of rating scale"));
								idVariable=rowInfo.get(rowStruct.indexOf("Variable ID"));


								if(idVariable.isEmpty() || idVariable.replaceAll("\"", "").isEmpty()){
										idVariable = cropID+":"+zero+cpt;//concatenate to create new ID
										cpt++;
								}

								try{
									Frame variable = m.setVariable(varName, idVariable, cropID+":Variable", coId, idMethod, idMeasure, crop, lang);
									//Frame variable = m.setVariable(varName, idVariable, cropID+":"+categoryVar, coId, idMethod, idMeasure, crop, lang);

									if(varSyn!= null && !varSyn.isEmpty() && !varSyn.replaceAll("\"", "").isEmpty()){
										   m.addAltLabel(varSyn, variable);
									 }
//									if(varDef!= null && !varDef.isEmpty() && !varDef.replaceAll("\"", "").isEmpty()){
//										m.addDef(varDef, variable);
//									}

								}catch(Exception e){
									System.out.println("Variable hasn't been created for this line. Need to create the variable manually or fix the problem in the file and re-run the script, line: "+ rowNumber);
								}
								//System.out.println(traitName+ method+ scaleName+ varName);
								//System.out.println("TOTOTAAAA!"+ variable);
							}else{
								System.out.println("Line "+ rowNumber + "has been ignored because Trait ID was empty. Fill in this column ans re-run the script to have this line info in the output obo file!");
								continue;
							}
						}
					}
				}

				m.save(args[1]);//m.save("/Users/marie-angeliquelaporte/Desktop/TEST.obo");
				System.out.println("FYI: List of duplicate IDs found in the file: "+duplicate);
				boolean deleted = temp.delete();
				if(!deleted){
					temp.deleteOnExit();
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	//xls to csv
	private static void xlsx(File inputFile, File outputFile) {
        // For storing data into CSV files
        StringBuffer data = new StringBuffer();

        try {
            FileOutputStream fos = new FileOutputStream(outputFile);
            // Get the workbook object for XLSX file
            Workbook wBook = WorkbookFactory.create(new FileInputStream(inputFile));
            // Get first sheet from the workbook
            Sheet sheet = wBook.getSheetAt(1);
           // Row row;
            //Cell cell;

            for(Row row : sheet) {
            	   for(int cn=0; cn<row.getLastCellNum(); cn++) {
            	       // If the cell is missing from the file, generate a blank one
            	       // (Works by specifying a MissingCellPolicy)
            	       Cell cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);

            	      // data.append("\""+cell.toString().trim() + "\""+ ";");
            	       data.append("\""+cell.toString().trim() + "\""+ "$");
            	   }
            	   data.append("\r\n");
            	}

            fos.write(data.toString().getBytes());
            fos.close();


        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }
}
