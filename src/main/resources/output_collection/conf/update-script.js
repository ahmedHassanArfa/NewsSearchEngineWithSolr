function processAdd(cmd) {
 doc = cmd.solrDoc;
 var previousDoc=null;
Data=doc.getFieldValue("text").toString();
 try{
 // create a term type object
 var Term = Java.type("org.apache.lucene.index.Term");
 var TermObject =new Term("id",doc.getFieldValue("rules").toString());
 //retrieve document id from solr return -1 if not present
 previousDocId= req.getSearcher().getFirstMatch(TermObject);
 if(-1!=perviousDocId)
{
// get complete document from solr for that id
 previousDoc=req.getSearcher().doc(previousDocId);
// get data from previous documnet
 previousData= previousDoc.getField("text").stringValue();
//  set in current after highlight document
 doc.setField("text",previousData+Data);
}
}
catch(err){
 
 logger.error("error in update processor "+err)
}
 
}
 
function processDelete(cmd) {
 // no-op
}
 
function processMergeIndexes(cmd) {
 // no-op
}
 
function processCommit(cmd) {
 // no-op
}
 
function processRollback(cmd) {
 // no-op
}
 
function finish() {
 // no-op
}
