package com.example.selectionofperformers.Entity;

public class Skills {
    private String idSkill;
    private String skillName;

    public Skills(String idSkill, String skillName) {
        this.idSkill = idSkill;
        this.skillName = skillName;

    }

    public String getSkillId() {
        return idSkill ;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillId() {
        this.idSkill = idSkill;
    }
    public void setSkillName() {
        this.skillName = skillName;
    }
}
