# save businessBanking.html to a .txt file by copy and paste

open (FH, "businessBanking.txt");
$total = 0;
while ($line = <FH>) {
    if ($line =~ /WIRE/) {
	print $line;
	@lines = split(/WIRE/, $line);
	$amounts =  $lines[1];

	$amounts =~ s/^\s+|\s+$//g;
	@amounts = split(/ /, $amounts);

	$amountMadeFormatted = $amounts[0];
	$amountMade = $amountMadeFormatted;
	$amountMade =~ s/\$|\,//;
	$total += $amountMade;
    }
}

$gross = $total / 1.05;
$gst = $total - $gross;
print "Total: $total\n";
print "Gross: $gross\n";
print "GST: $gst\n";
