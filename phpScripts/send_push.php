<?php

$conn = new mysqli("localhost", "root", "", "firebase");
$sql = "SELECT token FROM firebase";

$result = $conn->query($sql);
$tokens = array();

if ($result->num_rows > 0) {

    while ($row = $result->fetch_assoc()) {
        $tokens[] = $row["token"];
    }
}


$message = array("message" => "Message from server",
    "customKey" => "customValue");

$url = 'https://fcm.googleapis.com/fcm/send';
$fields = array(
    //'registration_ids' => $tokens, //tokens
    //"condition" => "'dogs' in topics || 'cats' in topics",
    "to" => "/topics/news",
    'data' => $message
);

$headers = array('Content-Type: application/json',
    'Authorization:key=AAAARXTrehs:APA91bExefFtL890BcFO4-Sq8_Kdrk251Sq_BM6WyC4bk-1cfD_s8gk-9-kW2iqm5ywaTM7JUh_yFQKWwuR_eYILaQR3ad0g--Fpd3Mr6sxHOtwnMKCFSNbQl3uahsBmVnH3mL2U7Xlwg3adH_QVF1yAiXpw8Nglmg'
);

$ch = curl_init($url);
curl_setopt($ch, CURLOPT_POST, true);
curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
$result = curl_exec($ch);

if ($result == FALSE)
    die('Curl failed: ' . curl_error($ch));

curl_close($ch);
$conn->close();

?>
