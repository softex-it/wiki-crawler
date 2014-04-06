#!/usr/bin/perl -w

open(my $fin, '<', "articles.txt");
chomp(my @lines = <$fin>);
close $fin;

open(my $fout, '>', 'articles-new.txt');

my $count = scalar(@lines);

foreach my $line (@lines) {

    my @article = split('  ', $line);
    
    my $art_length = scalar(@article);
    
    if ($art_length > 1) {
    	
        process_entry(@article);
        if (--$count) {
        	print $fout "\r\n";
        }

    }

}

sub process_entry {
	
	my @entry = @_;
	
	$title = $entry[0];
	$body = $entry[1];
	
	# Example: style="text-align:center;background-color:#C1D8FF;color:#000000"
	# $body =~ s/background:(.*?);/background:#202020;/g;
	
	# Remove all img tags
	$body =~ s/<img.*?>/ /g;
	
	print $fout "$title  $body";
	
}
