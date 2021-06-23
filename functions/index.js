// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access Firestore.
const admin = require('firebase-admin');
admin.initializeApp();

const db = admin.firestore();

// TODO1 市区町村ごとのグループ件数を測定するカウンターを設置する。
// TODO2 カウンターの件数がゼロになった際に全ての緯度・経度を削除する。 


//-----SAMPLE Take the text parameter passed to this HTTP endpoint and insert it into
exports.addMessage = functions.https.onRequest(async (req, res) => {
  const original = req.query.text;
  const writeResult = await admin.firestore().collection('messages').add({original: original});
  res.json({result: `Message with ID: ${writeResult.id} added.`});
});


//-----createGroup時にplaces-collectionに市区町村のLatitudeとLongitudeを設定
exports.createGroup = functions.firestore.document('groups/{groupId}')
.onCreate((snap, context) => {

  var newValue = snap.data();

  var prefectureAndCity = newValue.prefectureAndCity;
  var latitude = newValue.latitude;
  var longitude = newValue.longitude;

  var latitudeObject = {[prefectureAndCity]: latitude};
  var longitudeObject = {[prefectureAndCity]: longitude};
  //var countObject = {[prefectureAndCity]: FieldValue.incrememnt()};
  
  db.collection('places').doc(`japan`).set(
    {"latitude": latitudeObject}, 
    {merge:true})      

  db.collection('places').doc(`japan`).set(
    {"latitude": longitudeObject}, 
    {merge:true})      

  // db.collection('places').doc(`japan`).set(
  //   {"Count": longitudeObject}, 
  //   {merge:true})      

});