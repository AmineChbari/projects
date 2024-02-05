<!DOCTYPE html>
<html>
<head>
<title>South Motel</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://www.w3schools.com/lib/w3-theme-black.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Allerta+Stencil">
<style>
.w3-allerta {
  font-family: "Allerta Stencil", Sans-serif;
}
.w3-text-red1 {
    color:#ff0000;
}
.w3-red1 {
    background-color:#ff0000;
}
.w3-fate7 {
    color:#005580;
}
.w3-fate77 {
    background-color:#005580;
}
.w3-fato {
    background-color:#ffe6cc;
}
.w3-akhdori {
    background-color:#ace600;
}
.s-a {
  text-decoration: underline;
}
.table1, .th1, .td1 {
  border: 1px solid black;
  border-collapse: collapse;
}

</style>



</head>
<body id="myPage">
	<?php 
        include 'header_S.php';
        include 'menu_S.php';
       
         
        if (isset($_GET["page"])){
            include $_GET["page"];
        }
        else{
            
            include 'init.php';
        }

        include 'footer_S.php';
        
        ?>

    </body>
</html>