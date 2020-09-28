<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<!-- Title and other stuffs -->
<!-- <title>Dashboard - Lexicon Merchandise</title> -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="author" content="">

<style type="text/css">
	/*Now the CSS*/
	.tree * {margin: 0; padding: 0;}

	.tree ul {
		padding-top: 20px; position: relative;
		
		transition: all 0.5s;
		-webkit-transition: all 0.5s;
		-moz-transition: all 0.5s;
	}

	.tree li {
		float: left; text-align: center;
		list-style-type: none;
		position: relative;
		padding: 20px 5px 0 5px;
		
		transition: all 0.5s;
		-webkit-transition: all 0.5s;
		-moz-transition: all 0.5s;
	}

	/*We will use ::before and ::after to draw the connectors*/

	.tree li::before, .tree li::after{
		content: '';
		position: absolute; top: 0; right: 50%;
		border-top: 1px solid #8dc63f;
		width: 50%; height: 20px;
	}
	.tree li::after{
		right: auto; left: 50%;
		border-left: 1px solid #8dc63f;
	}

	/*We need to remove left-right connectors from elements without 
	any siblings*/
	.tree li:only-child::after, .tree li:only-child::before {
		display: none;
	}

	/*Remove space from the top of single children*/
	.tree li:only-child{ padding-top: 0;}

	/*Remove left connector from first child and 
	right connector from last child*/
	.tree li:first-child::before, .tree li:last-child::after{
		border: 0 none;
	}

	/*Adding back the vertical connector to the last nodes*/
	.tree li:last-child::before{
		border-right: 1px solid #8dc63f;
		border-radius: 0 5px 0 0;
		-webkit-border-radius: 0 5px 0 0;
		-moz-border-radius: 0 5px 0 0;
	}
	.tree li:first-child::after{
		border-radius: 5px 0 0 0;
		-webkit-border-radius: 5px 0 0 0;
		-moz-border-radius: 5px 0 0 0;
	}

	/*Time to add downward connectors from parents*/
	.tree ul ul::before{
		content: '';
		position: absolute; top: 0; left: 50%;
		border-left: 1px solid #8dc63f;
		width: 0; height: 20px;
	}

	.tree li a{
		border: 1px solid #8dc63f;
		padding: 1em 0.75em;
		text-decoration: none;
		color: #666767;
		font-family: arial, verdana, tahoma;
		font-size: 0.85em;
		display: inline-block;
		transition: all 0.5s;
		-webkit-transition: all 0.5s;
		-moz-transition: all 0.5s;
	}

	/* -------------------------------- */
	/* Now starts the vertical elements */
	/* -------------------------------- */

	.tree ul.vertical, ul.vertical ul {
	  padding-top: 0px;
	  left: 50%;
	}

	/* Remove the downward connectors from parents */
	.tree ul ul.vertical::before {
		display: none;
	}

	.tree ul.vertical li {
		float: none;
	  text-align: left;
	}

	.tree ul.vertical li::before {
	  right: auto;
	  border: none;
	}

	.tree ul.vertical li::after{
		display: none;
	}

	.tree ul.vertical li a{
		padding: 10px 0.75em;
	  margin-left: 16px;
	}

	.tree ul.vertical li::before {
	  top: -20px;
	  left: 0px;
		border-bottom: 1px solid #8dc63f;
		border-left: 1px solid #8dc63f;
		width: 20px; height: 60px;
	}

	.tree ul.vertical li:first-child::before {
	  top: 0px;
	  height: 40px;
	}

	/* Lets add some extra styles */

	div.tree > ul > li > ul > li > a {
	  width: 11em;
	}

	div.tree > ul > li > a {
	  font-size: 1em;
	  font-weight: bold;
	}


	/* ------------------------------------------------------------------ */
	/* Time for some hover effects                                        */
	/* We will apply the hover effect the the lineage of the element also */
	/* ------------------------------------------------------------------ */
	.tree li a:hover, .tree li a:hover+ul li a {
		background: #8dc63f;
	  color: white;
	  /* border: 1px solid #aaa; */
	}
	/*Connector styles on hover*/
	.tree li a:hover+ul li::after, 
	.tree li a:hover+ul li::before, 
	.tree li a:hover+ul::before, 
	.tree li a:hover+ul ul::before{
		border-color:  #aaa;
	}

</style>

<style type="text/css">
td img {
	display: block;
	margin-left: auto;
	margin-right: auto;
}

.centered {
	width: 50px;
	margin: 0px, auto, 0px, auto;
}
</style>

<!-- <script type="text/javascript"
	src="http://rawgit.com/vitmalina/w2ui/master/dist/w2ui.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="http://rawgit.com/vitmalina/w2ui/master/dist/w2ui.min.css" /> -->
	
<link href="${pageContext.request.contextPath}/resource/w2ui/w2ui.min.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/resource/w2ui/w2ui.min.js"></script>
</head>

<body>

	<input type="hidden" value="${pageContext.request.contextPath}"
		id="contextPath">

	<div class="page-head">
		<h2 class="pull-left" style="color: #428bca;">Dashboard | New Organogram - Synergy</h2>
		<div class="clearfix"></div>
	</div>

	

	<!--   <div class="matter"> -->
	<div class="container">

		<div class="row">

			<div class="col-md-12">

				<!-- Table -->

				<div class="row">

					<div class="col-md-12">

						<div class="widget">

							<div class="widget-head">
								<!-- <div class="pull-left">WELCOME</div> -->
								<!-- <div class="widget-icons pull-right">
									<a href="#" class="wminimize"><i class="fa fa-chevron-up"></i></a>
									<a href="#" class="wclose"><i class="fa fa-times"></i></a>
								</div> -->
								<!-- <div class="clearfix"></div> -->
							</div>

							<div class="widget-content">
								<div class="tree">
								    <ul>
								        <li>
								            <a href="#">CEO</a>
								            <ul>
								                <li>
								                    <a href="#">COO</a>
							
								                    <ul>
										                <li>
										                    <a href="#">Trade Business</a>
										                    <ul>
										                <li>
										                    <a href="#">Sales</a>
										                    <ul>
										                		<li>
										                		 	<a href="#">${organogram.tradeSales}</a>
										                		 	<ul>
												                		<li>
												                		 	<a href="#">Govt Sales</a>
												                		 	<ul class="vertical">
												                		 		<c:set var="govtSales" value="${organogram.govtSales}" />
												                		 		<c:forEach var="govt" items="${govtSales}">
												                		 			<li><a href="#">${govt}</a></li>
												                		 		</c:forEach>												                		 		
												                		 		 <li style="visibility: hidden;"><a href="#"></a></li>
														                       <!--  <li><a href="#">Alam</a></li>
														                        <li><a href="#">Towfiqe</a></li>
														                        <li><a href="#">Mahbub</a></li> -->
														                    </ul>
												                		</li>
												                		<li>
												                		 	<a href="#">Private Sales</a>
													                		 <ul class="vertical">
													                			 <c:set var="privateSales" value="${organogram.privateSales}" />
													                			 <c:forEach var="privates" items="${privateSales}">
													                			 	<li><a href="#">${privates}</a></li>
												                		 		</c:forEach>
														                        <!-- <li><a href="#">Khalid Jamil</a></li> -->
														                        <li style="visibility: hidden;"><a href="#"></a></li>
														                    </ul>
												                		</li>
												                	</ul>
										                		</li>
										                	</ul>
										                </li>
										                <li>
										                    <a href="#">Procurement</a>
										                    <ul>
										                		<li>
										                		 	<a href="#">${organogram.tradeProcurement}</a>
										                		</li>
										                	</ul>
										                </li>
							
										                <li>
										                    <a href="#">Customer Service</a>
										                    <ul>
										                		<li>
										                		 	<a href="#">${organogram.tradeCustomerService}</a>
										                		</li>
										                	</ul>
										                </li>
							
										                <li>
										                    <a href="#">Tender</a>
										                    <ul>
										                		<li>
										                		 	<a href="#">${organogram.tradeTender}</a>
										                		</li>
										                	</ul>
										                </li>
							
										                <li>
										                    <a href="#">HR &amp; Admin</a>
										                    <ul>
										                		<li>
										                		 	<a href="#">${organogram.tradeHrAdmin}</a>
										                		</li>
										                	</ul>
										                </li>
							
										                <li>
										                    <a href="#">Assets Dept</a>
										                    <ul>
										                		<li>
										                		 	<a href="#">${organogram.tradeAssets}</a>
										                		</li>
										                	</ul>
										                </li>
							
										                <li>
										                    <a href="#">Product Sourcing</a>
										                    <ul>
										                		<li>
										                		 	<a href="#">${organogram.tradeProductSource}</a>
										                		</li>
										                	</ul>
										                </li>
							
										                <li>
										                    <a href="#">Store &amp; Logistic</a>
										                    <ul>
										                		<li>
										                		 	<a href="#">${organogram.tradeStoreLog}</a>
										                		</li>
										                	</ul>
										                </li>
														
										                <li>
										                    <a href="#">Accounts</a>
										                    <ul>
										                		<li>
										                		 	<a href="#">${organogram.tradeAccounts}</a>
										                		</li>
										                	</ul>
										                </li>
							
										                <li>
										                    <a href="#">Import &amp; Export</a>
										                    <ul>
										                		<li>
										                		 	<a href="#">${organogram.tradeImportExport}</a>
										                		</li>
										                	</ul>
										                </li>
							
										                <li>
										                    <a href="#">Vat &amp; Tax</a>
										                    <ul>
										                		<li>
										                		 	<a href="#">${organogram.tradeVatTax}</a>
										                		</li>
										                	</ul>
										                </li>
							
										                <li>
										                    <a href="#">Operation</a>
										                    <ul>
										                		<li>
										                		 	<a href="#">${organogram.tradeOperation}</a>
										                		</li>
										                	</ul>
										                </li>
										            </ul>
										                </li>
							
										                <li>
										                    <a href="#">Emerging Business</a>
										                    <ul>
										                		<li>
												                    <a href="#">Accounts Emerging Business</a>
												                    <ul>
												                		<li>
												                		 	<a href="#">${organogram.emerAccounts}</a>
												                		</li>
												                	</ul>
												                </li>
							
												                <li>
												                    <a href="#">Procurement</a>
												                    <ul>
												                		<li>
												                		 	<a href="#">${organogram.emerProcurement}</a>
												                		</li>
												                	</ul>
												                </li>
												                <li>
												                    <a href="#">Product Sourcing</a>
												                    <ul>
												                		<li>
												                		 	<a href="#">${organogram.emerProductSource}</a>
												                		</li>
												                	</ul>
												                </li>
												                <li>
												                    <a href="#">Sales</a>
												                    <ul>
												                		<li>
												                		 	<a href="#">${organogram.emerSales}</a>
												                		</li>
												                	</ul>
												                </li>
												                <li>
												                    <a href="#">Store &amp; Logistic</a>
												                    <ul>
												                		<li>
												                		 	<a href="#">${organogram.emerStoreLog}</a>
												                		</li>
												                	</ul>
												                </li>
												                <li>
												                    <a href="#">Project Implementation</a>
												                    <ul>
												                		<li>
												                		 	<a href="#">${organogram.emerProjectImpl}</a>
												                		</li>
												                	</ul>
												                </li>
										                	</ul>
										                </li>			                
										            </ul>
								             	</li>
								             </ul>
								        </li>
								    </ul>
								</div>
								<!-- <h1 class="center red"> WELCOME! Please contact with Developer or HRD.</h1> -->
							</div>
							
							
							<div class="clearfix"></div>
							</div>
							
						</div>


					</div>

				</div>

			</div>

		</div>

	</div>


	<!-- Mainbar ends -->
	<div class="clearfix"></div>
</body>
</html>