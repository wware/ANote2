package es.uvigo.ei.sing.BioSearch.connection;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import es.uvigo.ei.pdfDownloader.PDFDownloadHandler;
import es.uvigo.ei.pdfDownloader.WebConnectionConsola;
import es.uvigo.ei.sing.BioSearch.datatypes.CollectionNCBI;
import es.uvigo.ei.sing.stringeditor.Transformer;
import es.uvigo.ei.sing.stringeditor.XMLInputOutput;

public class PubMex {
	
	static{
		Logger.getLogger(HttpMethodBase.class).setLevel(Level.OFF);
	}
//	static Logger logger = Logger.getLogger(PubMex.class.getName());
	static final int reintentos = 5;
	/*
	 * static EUtilsServiceStub service; static EFetchPubmedServiceStub
	 * pubService; static EUtilsServiceStub.ESearchRequest search_params = new
	 * EUtilsServiceStub.ESearchRequest(); static
	 * EUtilsServiceStub.ESummaryRequest summary_params = new
	 * EUtilsServiceStub.ESummaryRequest(); static EFetchRequest fetch_params =
	 * new EFetchRequest(); static ELinkRequest link_params = new
	 * ELinkRequest();
	 */

	static int count;
	static int block = 200;

	private boolean cancel = false;

	/*
	 * static { logger.info("starting services"); try{ service = new
	 * EUtilsServiceStub(); pubService = new EFetchPubmedServiceStub(); }catch
	 * (Exception e){ e.printStackTrace(); } }
	 */

	private static long lastQuery = 0;

	private static void waitIfNecessary() {

		while (System.currentTimeMillis() - lastQuery < 3000) {
			try {

				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		lastQuery = System.currentTimeMillis();
	}

	private CollectionNCBI postMedline(String webEnv, String queryKey, int min,
			int max, PubMexProgressHandler handler) {
		CollectionNCBI res = CollectionNCBI.createCollectionNCBI();
		res.addResultAttribute("Count"); // 0
		res.addResultAttribute("PMID"); // 1
		res.addResultAttribute("Title"); // 2
		res.addResultAttribute("Last Name"); // 3
		res.addResultAttribute("Fore Name"); // 4
		res.addResultAttribute("AuthorList"); // 5
		res.addResultAttribute("Abstract");// 6
		res.addResultAttribute("Publication Type");// 7
		res.addResultAttribute("Language");// 8
		res.addResultAttribute("Mesh Terms");// 9
		res.addResultAttribute("ISSN");// 10
		res.addResultAttribute("Publication Status");// 11
		res.addResultAttribute("Journal Title");// 12
		res.addResultAttribute("Pagination");// 13
		res.addResultAttribute("Volume");// 14
		res.addResultAttribute("Issue");// 15
		res.addResultAttribute("Publication Date");// 16

		handler.count(max);
		
		// nombre de la colección
		res.setName("PubMed_");
		int total = max - min;
		int nB = total / block;
		int resto = total % block;

		int getted = 0;
		for (int i = 0; i < nB; i++) {
			if (cancel == true) {
				handler.cancelled();
				break;
			}
			try {
				downloadPostMedline(res, webEnv, queryKey, min + i * block, block);
				
				getted += block;
				handler.getted(getted);
			} catch (Exception e) {
				handler.error(e);
			}
		}

		if (resto != 0 && cancel == false) {
			try {
				downloadPostMedline(res, webEnv, queryKey, min + (nB * block), resto);
				
				getted += resto;
				handler.getted(getted);
			} catch (Exception e) {
				handler.error(e);
			}
		} else if (cancel) {
			handler.cancelled();
		}
		
		return res;
	}

	private static void downloadPostMedline(CollectionNCBI res, String webEnv, String queryKey, int start, int max) 
	throws SAXException, IOException, ParserConfigurationException  {
		waitIfNecessary();
		String[] row = new String[res.getResultAttributes().size()];

		HttpClient client = new HttpClient();

		client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		
		// setting property
		if (System.getProperty("http.proxyHost") != null
				&& System.getProperty("http.proxyPort") != null) {
			client.getHostConfiguration().setProxy(
					System.getProperty("http.proxyHost"),
					Integer.parseInt(System.getProperty("http.proxyPort")));
		}

		
		PostMethod post = new PostMethod(
				"http://www.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi/");

		NameValuePair[] data = { 
			new NameValuePair("db", "pubmed"),
			new NameValuePair("WebEnv", webEnv),
			new NameValuePair("query_key", queryKey),
			new NameValuePair("retstart", Integer.toString(start)),
			new NameValuePair("retmax", Integer.toString(max)),
			new NameValuePair("retmode", "xml") 
		};
		post.setRequestBody(data);

		client.executeMethod(post);
		Document doc;
//		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(post.getResponseBodyAsStream());

			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			try {
				NodeList nodes = doc.getElementsByTagName("MedlineCitation");

				NodeList nodesPubMed = doc.getElementsByTagName("PubmedData");

				XPathExpression meshExpresion = xpath.compile("MeshHeadingList/MeshHeading/DescriptorName");
				XPathExpression lastNameExpresion = xpath.compile("Article/AuthorList/Author/LastName");
				XPathExpression foreNameExpresion = xpath.compile("Article/AuthorList/Author/ForeName");

				// XPathExpression initialsExpression =
				// xpath.compile("Article/AuthorList/Author/Initials");
				for (int j = 0; j < nodes.getLength(); j++) {
					row = new String[res.getResultAttributes().size()];
					// Count
					// TODO private static String getLineXML(Node node, String
					// string)
					// row[0]=getLineXML(nodes.item(j),"PMID");
					row[0] = Integer.toString(res.getResultSize());
					Element elements = (Element) nodes.item(j);
					Element pubElements = (Element) nodesPubMed.item(j);
					NodeList node;
					
					// Pubid
					// String pubid= getXMLElemnt(nodes.item(j),"PMID");
					node = elements.getElementsByTagName("PMID");
					row[1] = node.item(0).getTextContent();
					res.addID(node.item(0).getTextContent());
					
					// Article Title
					node = elements.getElementsByTagName("ArticleTitle");
					row[2] = node.item(0).getTextContent();

					// Last Name
					node = (NodeList) lastNameExpresion.evaluate(nodes.item(j),	XPathConstants.NODESET);
					String lastName = "";
					if (node.item(0) != null) {
						for (int x = 0; x < node.getLength(); x++) {
							lastName += node.item(x).getTextContent() + "//";
						}
						lastName = lastName.substring(0, lastName.length() - 2);
					}
					row[3] = lastName;

					// Fore Name
					node = (NodeList) foreNameExpresion.evaluate(nodes.item(j),
							XPathConstants.NODESET);
					String foreName = "";
					if (node.item(0) != null) {
						for (int x = 0; x < node.getLength(); x++) {
							foreName += node.item(x).getTextContent() + "//";
						}
						foreName = foreName.substring(0, foreName.length() - 2);
					}
					row[4] = foreName;

					// Author List
					String authorList = "";
					NodeList lastNameNode = (NodeList) lastNameExpresion.evaluate(nodes.item(j), XPathConstants.NODESET);
					NodeList initialsNode = (NodeList) elements.getElementsByTagName("Initials");

					if (initialsNode.item(0) != null) {
						for (int x = 0; x < initialsNode.getLength(); x++) {
							if (lastNameNode.item(x) == null)
								break;
							authorList += lastNameNode.item(x).getTextContent()
									+ " "
									+ initialsNode.item(x).getTextContent()
									+ ", ";
						}
						if (authorList.length() >= 1)
							authorList = authorList.substring(0, authorList.length() - 2);
					}
					row[5] = authorList;

					// Abstract
					node = elements.getElementsByTagName("AbstractText");
					if (node.item(0) != null) {
						row[6] = node.item(0).getTextContent();
					}

					// Publication Type
					node = elements.getElementsByTagName("PublicationType");
					if (node.item(0) != null) {
						row[7] = node.item(0).getTextContent();
					}

					// Language
					node = elements.getElementsByTagName("Language");
					row[8] = node.item(0).getTextContent();

					// Mesh Terms
					NodeList meshNode = (NodeList) meshExpresion.evaluate(nodes.item(j), XPathConstants.NODESET);
					String meshTerms = "";
					if (meshNode.item(0) != null) {
						for (int x = 0; x < meshNode.getLength(); x++) {
							meshTerms += meshNode.item(x).getTextContent() + "//";
						}
						meshTerms = meshTerms.substring(0, meshTerms.length() - 2);
					}
					row[9] = meshTerms;

					// ISSN
					node = elements.getElementsByTagName("ISSN");
					if (node.item(0) != null) {
						row[10] = node.item(0).getTextContent();
					}

					// Publication Estatus
					node = pubElements
							.getElementsByTagName("PublicationStatus");
					if (node.item(0) != null) {
						row[11] = node.item(0).getTextContent();
					}
					
					// journal title
					node = elements.getElementsByTagName("Title");
					if (node.item(0) != null) {
						row[12] = node.item(0).getTextContent();
					}

					// pagination
					node = elements.getElementsByTagName("MedlinePgn");
					if (node.item(0) != null) {
						row[13] = node.item(0).getTextContent();
					}

					// journal volume
					node = elements.getElementsByTagName("Volume");
					if (node.item(0) != null) {
						row[14] = node.item(0).getTextContent();
					}

					// journal issue
					node = elements.getElementsByTagName("Issue");
					if (node.item(0) != null) {
						row[15] = node.item(0).getTextContent();
					}

					// publication date
					node = elements.getElementsByTagName("PubDate");
					row[16] = "";
					if (node.item(0) != null) {
						NodeList childs = node.item(0).getChildNodes();
						for (int i = 0; i < childs.getLength(); i++) {
							if (childs.item(i).getTextContent().matches("[a-zA-Z0-9]+")) {
								row[16] += childs.item(i).getTextContent() + "-";
							}
						}
						if (row[16].endsWith("-")) {
							row[16] = row[16].substring(0, row[16].length() - 1);
						}
					}

					res.addResult(row);
				}
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
//		} catch (SAXException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//			throw new IOException(e1.getMessage());
//		} catch (ParserConfigurationException e1) {
//			// TODO Auto-generated catch block
//			throw new IOException(e1.getMessage());
//		}
	}

	private ESearchContext query(String terms) {
		try {
			terms = URLEncoder.encode(terms, "UTF-8");
			Transformer t = XMLInputOutput.loadTransformer(getClass().getResourceAsStream("search.xml"));
			String[] items = t.apply(new String[] { terms });
			return new ESearchContext(items[0], items[1], Integer.parseInt(items[2]));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void doQuery(String term, PubMexProgressHandler handler) {
		ESearchContext context = query(term);
		CollectionNCBI result;
//		try {
			result = postMedline(context.webEnv, context.queryKey, 0, context.count, handler);

			handler.finished(result);
//		} catch (IOException e) {
//			handler.error(e);
//		}
	}

	public void doQuery(String term, int max, PubMexProgressHandler handler) {
		ESearchContext context = query(term);
		CollectionNCBI result;

//		try {
			result = postMedline(context.webEnv, context.queryKey, 0, max, handler);
			handler.finished(result);
//
//		} catch (IOException e) {
//			handler.error(e);
//		}
	}
	
	public File downloadPDF(String pmid, String path, PDFDownloadHandler handler) {
		if (!path.endsWith("/")) {
			path += "/";
		}
		
//		DatosArticulo art = new DatosArticulo(pmid);
		if (WebConnectionConsola.buscarPDF(path, pmid, handler)) {
			return new File(path + "/" + pmid + "/" + pmid + ".pdf");
		} else {
			return null;
		}
	}

	public File downloadPDF(String pmid, String path) {
		if (!path.endsWith("/")) {
			path += "/";
		}
		
//		DatosArticulo art = new DatosArticulo(pmid);
		if (WebConnectionConsola.buscarPDF(path, pmid)) {
			return new File(path + "/" + pmid + "/" + pmid + ".pdf");
		} else {
			return null;
		}
	}

	public static void main(String args[]) throws IOException {
		final PubMex ex = new PubMex();
		ex.doQuery("DFP bioconductor", new PubMexProgressHandler() {
			public void cancelled() {
				System.out.println("cancelled");

			}

			public void count(int count) {
				System.out.println("Count: " + count);

			}

			public void error(Throwable error) {
				System.err.println(error);

			}

			public void finished(CollectionNCBI results) {
				System.out.println("finished");
				
				PDFDownloadHandler handler = new PDFDownloadHandler() {
					public void error(Exception exception) {
						this.error(null, exception);
					}
					
					public void error(String message) {
						this.error(message, null);
					}

					public void error(String message, Exception e) {
						if (message != null) System.err.println(message);
						if (e != null) e.printStackTrace();
					}

					public void finished(File file) {
						if (file == null) System.err.println("PDF couldn't be downloaded.");
						else System.out.println("Documents downloaded: " + file.getAbsolutePath());
					}

					public void info(String info) {
						System.out.println(info);
					}
				};
				
//				String pmid = results.getResultAt(0)[results
//						.getResultAttributes().indexOf("PMID")];
				String pmid = "18560603";
				File f = ex.downloadPDF(pmid, "./downloaded", handler);
				try {
					Runtime.getRuntime().exec("/usr/bin/kpdf " + f.getAbsolutePath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			public void getted(int getted) {
				System.out.print(" " + getted + "...");

			}
		});
	}

	public void cancel(boolean c) {
		cancel = true;
	}

	private class ESearchContext {
		final String webEnv;
		final String queryKey;
		final int count;

		public ESearchContext(final String webEnv, final String queryKey,
				int count) {
			super();
			this.webEnv = webEnv;
			this.queryKey = queryKey;
			this.count = count;
		}

		@Override
		public String toString() {
			return "context: " + this.webEnv + " " + this.queryKey;
		}
	}
}
