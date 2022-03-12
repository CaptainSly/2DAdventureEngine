package captainsly.adventure.core.editor.project;

/**
 * Prototyping a project system for the game editor
 *
 */
public class Project {

	private String projectName;

	public Project() {
	}

	public Project(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}
