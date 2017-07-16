package com.casic.simulation.planfile.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "SM_PLAN_FILE")
@SequenceGenerator(name = "SEQ_SM_PLAN_FILE_ID", sequenceName = "SEQ_SM_PLAN_FILE_ID",allocationSize=1,initialValue=1)
public class PlanFile implements Serializable {
	private static final long serialVersionUID = 1L;

	private long dbid;
    private String fileName;
	private String filePath;
    private String fileDisplayName;
    private String fileType;
    private Date upDateTimes;
    private String upPerson;

	@Column(name = "FILENAME ")
	public String getFileName()
	{
		return fileName;
	}
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	@Column(name = "FILEPATH")
	public String getFilePath()
	{
		return filePath;
	}
	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SM_PLAN_FILE_ID")
    public long getDbid() {
        return dbid;
    }

    public void setDbid(long dbid) {
        this.dbid = dbid;
    }

    @Column(name = "PLANFILETYPE")
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Column(name = "UPDATETIMES")
    public Date getUpDateTimes() {
        return upDateTimes;
    }

    public void setUpDateTimes(Date upDatetimes) {
        this.upDateTimes = upDatetimes;
    }

    @Column(name = "UPPERSON")
    public String getUpPerson() {
        return upPerson;
    }

    public void setUpPerson(String upPerson) {
        this.upPerson = upPerson;
    }

    @Column(name = "FILEDISPLAYNAME")
    public String getFileDisplayName() {
        return fileDisplayName;
    }

    public void setFileDisplayName(String fileDisplayName) {
        this.fileDisplayName = fileDisplayName;
    }
}
