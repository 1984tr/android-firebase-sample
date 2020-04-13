const admin = require('firebase-admin');
const functions = require('firebase-functions');

admin.initializeApp(functions.config().firebase);

const db = admin.firestore();

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

const replyRef = functions.firestore.document('feeds/{feedPath}/replies/{replyPath}')
exports.countReplyChange = replyRef.onCreate((snap, context) => {
    
    let feedPath = snap.ref.parent.parent.path
        
    let ref = db.doc(feedPath)
    let feedDoc = ref.get()
        .then(doc => {
            if (!doc.exists) {
                console.log('No such document!');
            } else {
                console.log('Document data:', doc.data());
            }
            return doc.data()
        })
        .catch(err => {
            console.log('Error getting document', err);
        });
});