package wad.domain;

import java.io.File;
import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


public class Searcher {
    private IndexSearcher indexSearcher;
    private QueryParser queryParser;
    private Query query;
    
    public Searcher(String indexDirectoryPath) throws IOException {
        Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath).toPath());
        DirectoryReader indexReader = DirectoryReader.open(indexDirectory);
        
        Analyzer analyzer = new StandardAnalyzer();
        
        this.indexSearcher = new IndexSearcher(indexReader);
        this.queryParser = new MultiFieldQueryParser(new String[] {"filename", "contents"}, analyzer);
    }
    
    public TopDocs search(String searchQuery) throws IOException, ParseException {
        this.query = this.queryParser.parse(searchQuery);
        return this.indexSearcher.search(query, 100);
    }
    
    public Document getDocument(ScoreDoc scoreDoc) throws CorruptIndexException, IOException {
        return this.indexSearcher.doc(scoreDoc.doc);
    }
    
    public void close() throws IOException {
        this.indexSearcher.getIndexReader().close();
    }
}