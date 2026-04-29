#Terraform (EKS + Karpenter)

module "karpenter" {
  source = "terraform-aws-modules/eks/aws//modules/karpenter"

  cluster_name = module.eks.cluster_name

  enable_irsa = true           #IAM Roles for Service Accounts
}