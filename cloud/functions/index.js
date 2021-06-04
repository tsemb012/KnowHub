
/*-----STEP1　必要なモジュールをインポートしてアプリを初期化する

これらの行によって firebase-functions および firebase-admin モジュールが読み込まれ、Cloud Firestoreの変更が可能なadminアプリインスタンスが初期化されます。
AdminSDKはFCM、Authentication、Firebase Realtime Databaseに対応しているため、これが利用可能な場所ではどこでも、Cloud Functionsを使用して効果的にFirebaseを統合できます。
Firebase CLI では、プロジェクトの初期化時に Firebase および Firebase SDK for Cloud Functions ノード モジュールが自動的にインストールされます。
サードパーティ ライブラリをプロジェクトに追加するには、package.json を変更して npm install を実行します。詳細については、依存関係を処理するをご覧ください。 */

// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access Firestore.
const admin = require('firebase-admin');
admin.initializeApp();





/*-----STEP2　addMessage() 関数を追加する

 addMessage() 関数は、HTTP エンドポイントです。エンドポイントに対するリクエストを行うと、
Express.JS スタイルの Request オブジェクトと Response オブジェクトが onRequest() コールバックに渡されます。
呼び出し可能な関数と同様に HTTP 関数は同期的です。このため、できるだけ早くレスポンスを送信し、Cloud Firestore による作業を遅らせる必要があります。
addMessage() HTTP 関数は、テキスト値を HTTP エンドポイントに渡し、/messages/:documentId/original パスの下でデータベースに挿入します。 */


// Take the text parameter passed to this HTTP endpoint and insert it into
// Firestore under the path /messages/:documentId/original
exports.addMessage = functions.https.onRequest(async (req, res) => {
    // Grab the text parameter.
    const original = req.query.text;
    // Push the new message into Firestore using the Firebase Admin SDK.
    const writeResult = await admin.firestore().collection('messages').add({original: original});
    // Send back a message that we've successfully written the message
    res.json({result: `Message with ID: ${writeResult.id} added.`});
  });




/*-----STEP3　makeUppercase() 関数を追加する

makeUppercase() 関数は、Cloud Firestore に書き込まれるときに実行されます。ref.set 関数ではリッスン対象のドキュメントを定義します。
パフォーマンス上の理由から、可能な限り具体的にする必要があります。中かっこ（{documentId} など）は、「パラメータ」を囲みます。
これは、コールバックで一致したデータを公開するワイルドカードです。
Cloud Firestore は、指定されたドキュメント上でデータの書き込みまたは更新が行われるたびに、onWrite() コールバックをトリガーします。

Cloud Firestore イベントなどのイベント ドリブンの関数は非同期です。
コールバック関数は、null、オブジェクト、Promise のいずれかを返す必要があります。
何も返さない場合、関数はタイムアウトし、エラーを通知し、再試行されます。*/


// Listens for new messages added to /messages/:documentId/original and creates an
// uppercase version of the message to /messages/:documentId/uppercase
exports.makeUppercase = functions.firestore.document('/messages/{documentId}')
.onCreate((snap, context) => {

  // Grab the current value of what was written to Firestore.
  const original = snap.data().original;

  // Access the parameter `{documentId}` with `context.params`
  functions.logger.log('Uppercasing', context.params.documentId, original);

  const uppercase = original.toUpperCase();

  // You must return a Promise when performing asynchronous tasks inside a Functions such as
  // writing to Firestore.
  // Setting an 'uppercase' field in Firestore document returns a Promise.
  return snap.ref.set({uppercase}, {merge: true});
});




// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//   functions.logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });

