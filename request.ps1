$URL = "http://localhost:8080/product/testrequest"

For ($i = 1; $i -le 20; $i++){
	Write-Outpout "Request $i "
	Invoke-WebRequest -Uri $URL -UseBasicParsing | Select-Object -ExpandProperty Content
	Write-Outpout "---------------------------"
	Start-Sleep -Seconds 1

}