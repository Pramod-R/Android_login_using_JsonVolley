<?php
	 include 'confignew.php';
	 $response = array();
	 
	 // Check whether username or password is set from android	
     if(isset($_POST['username']) && isset($_POST['password']))
     {
		  // Innitialize Variable
		  $result='';
	   	  $email = $_POST['username'];
                  $password = $_POST['password'];
		  
		  // Query database for row exist or not
          $sql = "SELECT * FROM register WHERE  fullname='$email' AND password='$password'";
          $res = mysqli_query($conn,$sql);
		   $rowcount=mysqli_num_rows($res);
		 
          
         if($rowcount>0)
{
	$response["fullname"]=$email;
		$response["pwd"]= $password;
	$response["msg"]="Sucess";
	
	
echo json_encode($response);
  	}
else
{

	$response["msg"]="Failure";
}
	}
?>
