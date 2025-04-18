package com.rosalind.domain.user;

import com.rosalind.domain.jpa.CreatedAtListener;
import com.rosalind.domain.jpa.SystemMetadata;
import com.rosalind.domain.jpa.UpdatedAtListener;
import lombok.*;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "rosalind_user", schema = "public")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@EntityListeners({CreatedAtListener.class, UpdatedAtListener.class})
public class RosalindUser implements SystemMetadata {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rosalind_user_seq")
  @SequenceGenerator(name = "rosalind_user_seq", sequenceName = "rosalind_user_seq", allocationSize = 1)
  @Column(name = "rosalind_user_id", nullable = false)
  private Long rosalindUserId;

  @Column(name = "service_type", nullable = false) // uk1
  @Enumerated(EnumType.STRING)
  private RosalindServiceType serviceType;

  @Column(name = "provider_code", nullable = false) // uk1
  @Enumerated(EnumType.STRING)
  private ProviderCode providerCode;

  @Column(name = "provider_id", nullable = false) // uk1
  private String providerId;

  @Column(name = "user_name")
  private String username;

  @Column(name = "user_uuid")
  private String userUuid;

  @Column(name = "email")
  private String email;

  @Column(name = "profile_url")
  private String profileUrl;

  @Column(name = "created_at", nullable = false)
  private Long createdAt;

  @Column(name = "updated_at", nullable = false)
  private Long updatedAt;

}
