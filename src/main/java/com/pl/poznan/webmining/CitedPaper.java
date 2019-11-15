package com.pl.poznan.webmining;

import java.util.Date;
import java.util.List;

// I joined github
// we are learning github and it's going good
// when we compare two cited papers we should just compare the titles --> Try to use comparator pattern to compare two of them...
public class CitedPaper {

	private Long paperID;
	private int pageNumber;
	private int positionOnPage;
	private String paperTitle;
	private String publicationVenu;
	private Date dateOfPublication;
	private int numOfCitations;
	private List<String> namesOfAuthors;
	
	public CitedPaper(int pageNumber, int positionOnPage, String paperTitle, String publicationVenu, Date dateOfPublication, int numOfCitations, List<String> namesOfAuthors ) {
		this.pageNumber = pageNumber;
		this.positionOnPage = positionOnPage;
		this.paperTitle = paperTitle;
		this.publicationVenu = publicationVenu;
		this.dateOfPublication = dateOfPublication;
		this.numOfCitations = numOfCitations;
		this.namesOfAuthors = namesOfAuthors;
	}

	public Long getPaperID() {
		return paperID;
	}

	public void setPaperID(Long paperID) {
		this.paperID = paperID;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPositionOnPage() {
		return positionOnPage;
	}

	public void setPositionOnPage(int positionOnPage) {
		this.positionOnPage = positionOnPage;
	}

	public String getPaperTitle() {
		return paperTitle;
	}

	public void setPaperTitle(String paperTitle) {
		this.paperTitle = paperTitle;
	}

	public String getPublicationVenu() {
		return publicationVenu;
	}

	public void setPublicationVenu(String publicationVenu) {
		this.publicationVenu = publicationVenu;
	}

	public Date getDateOfPublication() {
		return dateOfPublication;
	}

	public void setDateOfPublication(Date dateOfPublication) {
		this.dateOfPublication = dateOfPublication;
	}

	public int getNumOfCitations() {
		return numOfCitations;
	}

	public void setNumOfCitations(int numOfCitations) {
		this.numOfCitations = numOfCitations;
	}

	public List<String> getNamesOfAuthors() {
		return namesOfAuthors;
	}

	public void setNamesOfAuthors(List<String> namesOfAuthors) {
		this.namesOfAuthors = namesOfAuthors;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateOfPublication == null) ? 0 : dateOfPublication.hashCode());
		result = prime * result + ((namesOfAuthors == null) ? 0 : namesOfAuthors.hashCode());
		result = prime * result + numOfCitations;
		result = prime * result + pageNumber;
		result = prime * result + ((paperID == null) ? 0 : paperID.hashCode());
		result = prime * result + ((paperTitle == null) ? 0 : paperTitle.hashCode());
		result = prime * result + positionOnPage;
		result = prime * result + ((publicationVenu == null) ? 0 : publicationVenu.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CitedPaper other = (CitedPaper) obj;
		if (dateOfPublication == null) {
			if (other.dateOfPublication != null)
				return false;
		} else if (!dateOfPublication.equals(other.dateOfPublication))
			return false;
		if (namesOfAuthors == null) {
			if (other.namesOfAuthors != null)
				return false;
		} else if (!namesOfAuthors.equals(other.namesOfAuthors))
			return false;
		if (numOfCitations != other.numOfCitations)
			return false;
		if (pageNumber != other.pageNumber)
			return false;
		if (paperID == null) {
			if (other.paperID != null)
				return false;
		} else if (!paperID.equals(other.paperID))
			return false;
		if (paperTitle == null) {
			if (other.paperTitle != null)
				return false;
		} else if (!paperTitle.equals(other.paperTitle))
			return false;
		if (positionOnPage != other.positionOnPage)
			return false;
		if (publicationVenu == null) {
			if (other.publicationVenu != null)
				return false;
		} else if (!publicationVenu.equals(other.publicationVenu))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CitedPaper [pageNumber=" + pageNumber + ", positionOnPage=" + positionOnPage + ", paperTitle="
				+ paperTitle + ", publicationVenu=" + publicationVenu + ", dateOfPublication=" + dateOfPublication
				+ ", numOfCitations=" + numOfCitations + ", namesOfAuthors=" + namesOfAuthors + "]";
	}

}
