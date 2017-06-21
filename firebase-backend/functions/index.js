/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
'use strict';

// [START all]
// [START import]
// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database. 
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);
// [END import]

// [START addMessage]
// Take the text parameter passed to this HTTP endpoint and insert it into the
// Realtime Database under the path /messages/:pushId/original
// [START addMessageTrigger]
exports.addMessage = functions.https.onRequest((req, res) => {
    // [END addMessageTrigger]
    // Grab the text parameter.
    const original = req.query.text;
    // [START adminSdkPush]
    // Push the new message into the Realtime Database using the Firebase Admin SDK.
    admin.database().ref('/messages').push({
        original: original
    }).then(snapshot => {
        // Redirect with 303 SEE OTHER to the URL of the pushed object in the Firebase console.
        res.redirect(303, snapshot.ref);
    });
    // [END adminSdkPush]
});
// [END addMessage]

// [START makeUppercase]
// Listens for new messages added to /messages/:pushId/original and creates an
// uppercase version of the message to /messages/:pushId/uppercase
// [START makeUppercaseTrigger]
exports.process = functions.database.ref('/users/expensesqueue')
    .onWrite(event => {
        var request = require('request');

        function move(expense, userq, dateq) {

            var date = expense.child("reportedWhen").val();
            //console.log(date,admin.database.ServerValue.TIMESTAMP.getMilliseconds());
            
            if (new Date(date + 84000000) < new Date()) {

                var dateurl = new Date(date).toISOString().slice(0, 10);
                var url = 'http://apilayer.net/api/historical?access_key=13fbbbd86cf83d0a8e9f2606c41cb637&date=' + dateurl + '&currencies=USD,UYU&format=1';

                request.get({
                    url: url,
                    json: true,
                    headers: {
                        'User-Agent': 'request'
                    }
                }, (err, res, data) => {
                    if (err) {
                        console.log('Error:', err);
                    } else if (res.statusCode !== 200) {
                        console.log('Status:', res.statusCode);
                    } else {
                        // data is already parsed as JSON:
                        var cotizacion = data.quotes.USDUYU;
                        var amount = expense.child("amount").val();
                        var category = expense.child("categoryId").val();
                        var id = expense.child("id").val();
                        var moneda = expense.child("moneda").val();
                        var note = expense.child("note").val();


                        admin.database().ref('/users/' + userq + '/expenses/' + dateq + '/' + id).set({
                            amount: String(amount / cotizacion),
                            categoryId: category,
                            id: id,
                            moneda: moneda,
                            amountO: amount,
                            note: note,
                            reportedWhen: date,
                            cotizado: cotizacion

                        });
                        
                         admin.database().ref('/users/expensesqueue/' + userq + '/' + dateq + '/' + id).remove();


                       
                    }
                });


            } else {
                var dateurl = new Date(date).toISOString().slice(0, 10);
                var url = 'http://free.currencyconverterapi.com/api/v3/convert?q=USD_UYU&compact=ultra';

                request.get({
                    url: url,
                    json: true,
                    headers: {
                        'User-Agent': 'request'
                    }
                }, (err, res, data) => {
                    if (err) {
                        console.log('Error:', err);
                    } else if (res.statusCode !== 200) {
                        console.log('Status:', res.statusCode);
                    } else {
                        // data is already parsed as JSON:
                        var cotizacion = data.USD_UYU;
                        var amount = expense.child("amount").val();
                        var category = expense.child("categoryId").val();
                        var id = expense.child("id").val();
                        var moneda = expense.child("moneda").val();
                        var note = expense.child("note").val();


                        admin.database().ref('/users/' + userq + '/expenses/' + dateq + '/' + id).set({
                            amount: String(amount / cotizacion),
                            categoryId: category,
                            id: id,
                            moneda: moneda,
                            amountO: amount,
                            note: note,
                            reportedWhen: date,
                            cotizado: cotizacion

                        });
                        
                        admin.database().ref('/users/expensesqueue/' + userq + '/' + dateq + '/' + id).remove();

                       
                    }
                });

            }
        }
    function moved(expense, userq, dateq){
        
                var date = expense.child("reportedWhen").val();
                        var amount = expense.child("amount").val();
                        var category = expense.child("categoryId").val();
                        var id = expense.child("id").val();
                        var moneda = expense.child("moneda").val();
                        var note = expense.child("note").val();


                        admin.database().ref('/users/' + userq + '/expenses/' + dateq + '/' + id).set({
                            amount: String(amount),
                            categoryId: category,
                            id: id,
                            moneda: moneda,
                            amountO: amount,
                            note: note,
                            reportedWhen: date });
                        
                        admin.database().ref('/users/expensesqueue/' + userq + '/' + dateq + '/' + id).remove();

    }








        const original = event.data.val();
        console.log('Procesando...', original);


        event.data.forEach(function (userwaiting) {

            userwaiting.forEach(function (dateadd) {


                dateadd.forEach(function (expense) {
                    var money = expense.child("moneda").val();
                    if (money == 'pesos') {
                        move(expense, userwaiting.key, dateadd.key);

                    } else if (money == 'usd') {
                        moved(expense, userwaiting.key, dateadd.key);
                    }
                    // Cancel enumeration

                });
                // Cancel enumeration
            });

        });
        // Cancel enumeration
        // Setting an "uppercase" sibling in the Realtime Database returns a Promise.
        return true;
        // [END makeUppercaseBody]
    });
// [END makeUppercase]
// [END all]
