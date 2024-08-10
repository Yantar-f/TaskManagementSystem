package ru.effectivemobile.taskmanagementsystem.model;

public class TokenBody {
    private Long subjectId;

    public TokenBody(Long subjectId) {

        this.subjectId = subjectId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public int hashCode() {
        return subjectId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;

        TokenBody tokenClaims = (TokenBody) obj;

        return  subjectId.equals(tokenClaims.subjectId);
    }
}
