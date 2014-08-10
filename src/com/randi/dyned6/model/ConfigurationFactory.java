package com.randi.dyned6.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.rim.device.api.xml.parsers.DocumentBuilder;
import net.rim.device.api.xml.parsers.DocumentBuilderFactory;
import net.rim.device.api.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.awan.dyned6.general.Constans;

/**
 * Configuration factory parsing from XML file appconfig.xml.
 * @author Randi Waranugraha
 *
 */
public class ConfigurationFactory {

	private ConfigurationListener myListener;

	public ConfigurationFactory(ConfigurationListener configurationListener) {
		myListener = configurationListener;
	}

	public void parseXML() {
		InputStream stream = (InputStream) this.getClass().getResourceAsStream("/raw/appconfig.xml");

		byte[] buffer = new byte[1024];
		StringBuffer sb = new StringBuffer();
		int readIn = 0;

		try {
			while ((readIn = stream.read(buffer)) > 0) {
				String temp = new String(buffer, 0, readIn);
				sb.append(temp);
			}

			String result = sb.toString();
			ByteArrayInputStream bis = new ByteArrayInputStream(result.getBytes("UTF-8"));
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(bis);
			doc.getDocumentElement().normalize();
			
			NodeList appConfigXml = doc.getElementsByTagName("mainlist");
			System.out.println("configXML length " + appConfigXml.getLength());
			for (int i = 0; i < appConfigXml.getLength(); i++) {

				NodeList childNode = appConfigXml.item(i).getChildNodes();

				if (Constans.mainScreenList == null) {
					Constans.mainScreenList = new String[12];
					Constans.subCategory = new String[12][4];
					Constans.audioList = new String[12][4];
					Constans.imageList = new String[12][4];
					Constans.scriptList = new String[12][4];
					Constans.comprehensionItem = new String[12][4][3][8];
					Constans.grammarItem = new String[12][4][3][8];
				}

				int b = 0;

				for (int j = 0; j < childNode.getLength(); j++) {
					String strChildNodeName = childNode.item(j).getNodeName();

					if (strChildNodeName.equalsIgnoreCase("appname")) {
						Constans.mainHeaderTitle = childNode.item(j).getChildNodes().item(0).getNodeValue();
					} else if (strChildNodeName.equalsIgnoreCase("mainlistitem")) {
						NodeList unitNode = childNode.item(j).getChildNodes();
					
						if (Constans.subCategory == null) {
							/*
							 * Constans.subCategory = new String[12][4];
							 * 
							 * Constans.audioList = new String[12][4];
							 * 
							 * Constans.imageList = new String[12][4];
							 * 
							 * Constans.scriptList = new String[12][4];
							 */
						}

						for (int k = 0; k < unitNode.getLength(); k++) {
							String strUnitNodeName = unitNode.item(k).getNodeName();

							if (strUnitNodeName.equalsIgnoreCase("title")) {
								Constans.mainScreenList[b] = unitNode.item(k).getChildNodes().item(0).getNodeValue();
							} else if (strUnitNodeName.equalsIgnoreCase("lesson")) {
								NodeList lessonUnitNode = unitNode.item(k).getChildNodes();
								int a = 0;
								for (int l = 0; l < lessonUnitNode.getLength(); l++) {
									String lessonItemNodeName = lessonUnitNode.item(l).getNodeName();

									if (lessonItemNodeName.equalsIgnoreCase("lessonitem")) {
										NodeList lessonUnitChildNode = lessonUnitNode.item(l).getChildNodes();
										String lessonTitle = null;
										String lessonImage = null;
										String lessonAudio = null;
										String lessonScript = null;
										/*
										 * 
										 * Looping for /lesson in /unit
										 */

										for (int m = 0; m < lessonUnitChildNode.getLength(); m++) {
											String lessonItemChildNodeName = lessonUnitChildNode.item(m).getNodeName().trim();

											if (lessonItemChildNodeName.equalsIgnoreCase("title")) {
												lessonTitle = lessonUnitChildNode.item(m).getChildNodes().item(0).getNodeValue().trim();
											} else if (lessonItemChildNodeName.equalsIgnoreCase("image")) {
												lessonImage = lessonUnitChildNode.item(m).getChildNodes().item(0).getNodeValue().trim();
											} else if (lessonItemChildNodeName.equalsIgnoreCase("audio")) {
												lessonAudio = lessonUnitChildNode.item(m).getChildNodes().item(0).getNodeValue().trim();
											} else if (lessonItemChildNodeName.equalsIgnoreCase("viewscript")) {
												lessonScript = lessonUnitChildNode.item(m).getChildNodes().item(0).getNodeValue().trim();
											} else if (lessonItemChildNodeName.equalsIgnoreCase("comprehension")) {
												NodeList comprehensionList = lessonUnitChildNode.item(m).getChildNodes();
												int ab = 0;
												String compType = Constans.compTextOnly;
												int c = 0;
												for (int n = 0; n < comprehensionList.getLength(); n++) {
													String comprehensionItemChildNodeName = comprehensionList.item(n).getNodeName().trim();

													if (comprehensionItemChildNodeName.equalsIgnoreCase("type")) {
														compType = comprehensionList.item(n).getChildNodes().item(0).getNodeValue().trim();
													} else if (comprehensionItemChildNodeName.equalsIgnoreCase("comprehensionitem")) {
														String[] tmpComprehension = new String[6];
														NodeList comprehensionItem = comprehensionList.item(n).getChildNodes();
														tmpComprehension[ab] = compType;
														ab++;

														for (int o = 0; o < comprehensionItem.getLength(); o++) {
															String comprehensionItemName = comprehensionItem.item(o).getNodeName().trim();

															if (comprehensionItemName.equalsIgnoreCase("question")) {
																tmpComprehension[ab] = comprehensionItem.item(o).getChildNodes().item(0).getNodeValue().trim();
																ab++;
															} else if (comprehensionItemName.equalsIgnoreCase("option")) {
																tmpComprehension[ab] = comprehensionItem.item(o).getChildNodes().item(0).getNodeValue().trim();
																ab++;
															} else if (comprehensionItemName.equalsIgnoreCase("answer")) {
																tmpComprehension[ab] = comprehensionItem.item(o).getChildNodes().item(0).getNodeValue().trim();
																ab++;
															}
														}

														Constans.comprehensionItem[b][a][c] = tmpComprehension;
														ab = 0;
														c++;
													}
												}
											} else if (lessonItemChildNodeName.equalsIgnoreCase("grammar")) {
												NodeList grammarList = lessonUnitChildNode.item(m).getChildNodes();
												int ac = 0;
												String grammarType = Constans.grammarTextOnly;
												int c = 0;
												
												for (int n = 0; n < grammarList.getLength(); n++) {
													String grammarItemChildNodeName = grammarList.item(n).getNodeName();

													if (grammarItemChildNodeName.equalsIgnoreCase("type")) {
														grammarType = grammarList.item(n).getChildNodes().item(0).getNodeValue();
													} else if (grammarItemChildNodeName.equalsIgnoreCase("grammaritem")) {
														String[] tmpGrammar = new String[8];
														NodeList grammarItem = grammarList.item(n).getChildNodes();
														tmpGrammar[ac] = grammarType;
														ac++;

														for (int o = 0; o < grammarItem.getLength(); o++) {
															String comprehensionItemName = grammarItem.item(o).getNodeName().trim();

															if (comprehensionItemName.equalsIgnoreCase("question")) {
																tmpGrammar[ac] = grammarItem.item(o).getChildNodes().item(0).getNodeValue().trim();
																ac++;
															} else if (comprehensionItemName.equalsIgnoreCase("option")) {
																tmpGrammar[ac] = grammarItem.item(o).getChildNodes().item(0).getNodeValue().trim();
																ac++;
															} else if (comprehensionItemName.equalsIgnoreCase("answer")) {
																tmpGrammar[ac] = grammarItem.item(o).getChildNodes().item(0).getNodeValue().toString().trim();
																ac++;
															}
														}

														Constans.grammarItem[b][a][c] = tmpGrammar;
														ac = 0;
														c++;
													}
												}
											}
										}
										Constans.subCategory[b][a] = lessonTitle;
										Constans.audioList[b][a] = lessonAudio;
										Constans.imageList[b][a] = lessonImage;
										Constans.scriptList[b][a] = lessonScript;
										a++;
									}
								}
							}
						}
						b++;
					}// end mainlist
				}
			}// end main xml
			myListener.onDone();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IOException ======= " + e.getMessage());
			myListener.onError(e.getMessage());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.out.println("ParserConfigurationException ======= " + e.getMessage());
			myListener.onError(e.getMessage());
		} catch (SAXException e) {
			e.printStackTrace();
			System.out.println("SAXException ======= " + e.getMessage());
			myListener.onError(e.getMessage());
		} 
	}
}