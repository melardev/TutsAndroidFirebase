<?php

if (isset($_POST["token"])) {

    $token = $_POST["token"];
    $conn = new mysqli('127.0.0.1', 'root', '', 'firebase');

    /*$query = "CREATE DATABASE IF NOT EXISTS firebase;"
                "CREATE TABLE IF NOT EXISTS `firebase` (
      `id` int(11) NOT NULL,
      `token` varchar(100) NOT NULL,
      PRIMARY KEY(`id`),
      UNIQUE (`token`)
    ) ENGINE=InnoDB DEFAULT CHARSET=latin1;";

    $conn->multi_query($query);
    $result = $conn->store_result();
    $rows = $result->fetch_all();*/
    $q = "INSERT IGNORE INTO `firebase` SET `token`= ?;";
    $stmt = $conn->prepare($q);

    if (!$stmt)
        exit();

    $stmt->bind_param("s", $token);
    if ($stmt->execute())
        echo "Success!";

    $stmt->close();
    $conn->close();
}
?>
