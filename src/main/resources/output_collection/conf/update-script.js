function processAdd(cmd) {
 doc = cmd.solrDoc;
 //var previousDoc=null;
 text=doc.getFieldValue("text").toString();
 updatedtext = text;
 rules=doc.getFieldValue("rules").toString();
 rulesArr = rules.split(",");
for (i = 0; i < rulesArr.length; i++) {
  rule = rulesArr[i];
  updatedtext = updatedtext.replace(rule, "<b>" + rule + "</b>" );
}

 try{
 doc.setField("text",updatedtext);

 // create a term type object
 //var Term = Java.type("org.apache.lucene.index.Term");
 //var TermObject =new Term("id",doc.getFieldValue("rules").toString());
 //retrieve document id from solr return -1 if not present
 //previousDocId= req.getSearcher().getFirstMatch(TermObject);
 //if(-1!=perviousDocId)
//{
// get complete document from solr for that id
 //previousDoc=req.getSearcher().doc(previousDocId);
// get data from previous documnet
 //previousData= previousDoc.getField("text").stringValue();
//  set in current after highlight document
 //doc.setField("text",updatedtext);
//}
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
