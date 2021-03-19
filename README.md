# Demo-SAP-Spring-MVC-Project
This is a demo Spring based project without Front-End, only Back-End. The Front-End is reproducted by command-based AppInitializer class. The description of the commends are below.
The content is published by Vladimir Dobrev.

COMMAND - EXAMPLES:

Register|adminName|adminPass|adminConfirPass|adminEmail
Register|Ivan|Ivan12|Ivan12|ivanpicha@abv.bg

Login|Role|email|pass
Login|Administrator|ivanpicha@abv.bg|Ivan12
Login|Seller|pencho13@abv.bg|Pencho123

AddProduct|productName|productPrice
AddProduct|OakLeavesOil|12500.456

AddProduct|oldProductName|newProductName|newProductPrice
EditProduct|OakLeavesOil|OOOLeavesOil|10000

DeleteProduct|productName
DeleteProduct|OOOLeavesOil

AddSeller|sellerName|sellerPass|sellerEmail
AddSeller|Pencho|Pencho123|pencho13@abv.bg

EditSeller|oldSellerName|newSellerName|newPassword|email
EditSeller|Pencho|Peter|Peter123|peter12@abv.bg

DeleteSeller|sellerEmail
DeleteSeller|peter12@abv.bg

AddClient|newClientName|newClientEmail
AddClient|George|george12@abv.bg

EditClient|oldClientName|newClientName|newClientEmail
EditClient|George|Petar|petar12@abv.bg

DeleteClient|clientName
DeleteClient|Petar

AddSale|Date|clientName|nameProduct
AddSale|13/03/2020|George|akLeavesOil

ViewSalesForPeriod|firstDate|lastDate
ViewSalesForPeriod|12/03/2020|15/03/2020

ViewSalesBySeller|sellerName
ViewSalesBySeller|Pencho
 
